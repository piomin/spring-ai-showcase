package pl.piomin.services.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import pl.piomin.services.model.ImageDescription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final static Logger LOG = LoggerFactory.getLogger(ImageController.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final ChatClient chatClient;
    private final ImageModel imageModel;
    private final VectorStore store;
    private List<Media> images;
    private List<Media> dynamicImages = new ArrayList<>();

    public ImageController(ChatClient.Builder chatClientBuilder,
                           ImageModel imageModel,
                           VectorStore store) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.imageModel = imageModel;
        this.store = store;

        this.images = List.of(
                Media.builder().id("fruits").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/fruits.png")).build(),
                Media.builder().id("fruits-2").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/fruits-2.png")).build(),
                Media.builder().id("fruits-3").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/fruits-3.png")).build(),
                Media.builder().id("fruits-4").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/fruits-4.png")).build(),
                Media.builder().id("fruits-5").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/fruits-5.png")).build(),
                Media.builder().id("animals").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/animals.png")).build(),
                Media.builder().id("animals-2").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/animals-2.png")).build(),
                Media.builder().id("animals-3").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/animals-3.png")).build(),
                Media.builder().id("animals-4").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/animals-4.png")).build(),
                Media.builder().id("animals-5").mimeType(MimeTypeUtils.IMAGE_PNG).data(new ClassPathResource("images/animals-5.png")).build()
        );
    }

    @GetMapping("/load")
    void load() throws JsonProcessingException {
        String msg = """
        Explain what do you see on the image.
        Generate a compact description that explains only what is visible.
        """;
        for (Media image : images) {
            UserMessage um = new UserMessage(msg, image);
            String content = this.chatClient.prompt(new Prompt(um))
                    .call()
                    .content();

            var doc = Document.builder()
                    .id(image.getId())
                    .text(mapper.writeValueAsString(new ImageDescription(image.getId(), content)))
                    .build();
            store.add(List.of(doc));
            LOG.info("Document added: {}", image.getId());
        }
    }

    @GetMapping(value = "/find/{object}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody byte[] analyze(@PathVariable String object) {
        String msg = """
        Which picture contains %s.
        Return only a single picture.
        Return only the number that indicates its position in the media list.
        """.formatted(object);
        LOG.info(msg);

        UserMessage um = new UserMessage(msg, images);

        String content = this.chatClient.prompt(new Prompt(um))
                .call()
                .content();

        assert content != null;
        return images.get(Integer.parseInt(content)-1).getDataAsByteArray();
    }

    @GetMapping(value = "/generate/{object}", produces = MediaType.IMAGE_PNG_VALUE)
    byte[] generate(@PathVariable String object) throws IOException {
        ImageResponse ir = imageModel.call(new ImagePrompt("Generate an image with " + object, ImageOptionsBuilder.builder()
                .height(1024)
                .width(1024)
                .N(1)
                .responseFormat("url")
                .build()));
        UrlResource url = new UrlResource(ir.getResult().getOutput().getUrl());
        LOG.info("Generated URL: {}", ir.getResult().getOutput().getUrl());
        dynamicImages.add(Media.builder()
                .id(UUID.randomUUID().toString())
                .mimeType(MimeTypeUtils.IMAGE_PNG)
                .data(url)
                .build());
        return url.getContentAsByteArray();
    }

    @GetMapping("/describe")
    String[] describe() {
        UserMessage um = new UserMessage("Explain what do you see on each image.",
                List.copyOf(Stream.concat(images.stream(), dynamicImages.stream()).toList()));
        return this.chatClient.prompt(new Prompt(um))
                .call()
                .entity(String[].class);
    }

    @GetMapping("/generate-and-match/{object}")
    List<Document> generateAndMatch(@PathVariable String object) throws IOException {
        ImageResponse ir = imageModel.call(new ImagePrompt("Generate an image with " + object, ImageOptionsBuilder.builder()
                .height(1024)
                .width(1024)
                .N(1)
                .responseFormat("url")
                .build()));
        UrlResource url = new UrlResource(ir.getResult().getOutput().getUrl());
        LOG.info("URL: {}", ir.getResult().getOutput().getUrl());

        String msg = """
        Explain what do you see on the image.
        Generate a compact description that explains only what is visible.
        """;

        UserMessage um = new UserMessage(msg, new Media(MimeTypeUtils.IMAGE_PNG, url));
        String content = this.chatClient.prompt(new Prompt(um))
                .call()
                .content();

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Find the most similar description to this: " + content)
                .topK(2)
                .build();

        return store.similaritySearch(searchRequest);

//        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
//                .documentRetriever(VectorStoreDocumentRetriever.builder()
//                        .similarityThreshold(0.7)
//                        .topK(3)
//                        .vectorStore(store)
//                        .build())
//                .build();
//
//        return this.chatClient.prompt(new Prompt(um))
//                .advisors(retrievalAugmentationAdvisor)
//                .call()
//                .content();
    }

}
