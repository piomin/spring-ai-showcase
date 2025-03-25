# Spring AI Showcase Demo Project [![Twitter](https://img.shields.io/twitter/follow/piotr_minkowski.svg?style=social&logo=twitter&label=Follow%20Me)](https://twitter.com/piotr_minkowski)

[![CircleCI](https://circleci.com/gh/piomin/spring-ai-showcase.svg?style=svg)](https://circleci.com/gh/piomin/spring-ai-showcase)

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=piomin_spring-ai-showcase)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=piomin_spring-ai-showcase&metric=bugs)](https://sonarcloud.io/dashboard?id=piomin_spring-ai-showcase)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=piomin_spring-ai-showcase&metric=coverage)](https://sonarcloud.io/dashboard?id=piomin_spring-ai-showcase)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=piomin_spring-ai-showcase&metric=ncloc)](https://sonarcloud.io/dashboard?id=piomin_spring-ai-showcase)

This project demonstrates the integration of AI capabilities within a Spring Boot application, utilizing the [Spring AI](https://github.com/spring-projects/spring-ai) framework.

-----

## Table of Contents

- [Architecture](#architecture)
- [Running the Application](#running-the-application)
- [Articles](#articles)

## Architecture

Currently, there are four `@RestController`s that show Spring AI features:

`pl.piomin.services.controller.PersonController` - prompt template, chat memory, and structured output based on a simple example that asks AI model to generate some persons

`pl.piomin.services.controller.WalletController` - function calling that calculates a value of our wallet stored in local database in conjunction with the latest stock prices

`pl.piomin.services.controller.StockController` - RAG with a Pinecone vector store and OpenAI based on stock prices API

`pl.piomin.services.controller.ImageController` - image model and multimodality

The architecture is designed to be modular and scalable, focusing on demonstrating how AI features can be incorporated into Spring-based applications.

## Running the Application

Follow these steps to run the application locally. 
```bash
git clone https://github.com/piomin/spring-ai-showcase.git
cd spring-ai-showcase
```

By default, this sample Spring AI app connects to OpenAI. So, before running the app you must set a token:
```shell
export OPEN_AI_TOKEN=<YOUR_API_TOKEN>
mvn spring-boot:run
```

To enable integration with Mistral, we should activate the `mistral-ai` profile: 
```shell
export MISTRAL_AI_TOKEN=<YOUR_API_TOKEN>
mvn spring-boot:run -Pmistral-ai
```

To enable integration with Ollama, we should activate the `ollama-ai` profile:
```shell
mvn spring-boot:run -Pollama-ai
```

Before that, we must run the model on Ollama, e.g.:
```shell
ollama run llava
```

To enable integration with Azure OpenAI, we should activate the `azure-ai` profile and activate the Spring Boot `azure-ai` profile:
```shell
mvn spring-boot:run -Pazure-ai -Dspring-boot.run.profiles=azure-ai
```

You should also export the Azure OpenAI credentials:
```shell
export AZURE_OPENAI_API_KEY=<YOUR_AZURE_OPENAI_API_KEY>
```

For scenarios with a vector store (`StockController`, `ImageController`) you need to export the following ENV:
```shell
export PINECONE_TOKEN=<YOUR_PINECONE_TOKEN>
```

For scenarios with a stock API (`StockController`, `WalletController`) you need to export the following ENV:
```shell
export STOCK_API_KEY=<YOUR_STOCK_API_KEY>
```

More details in the articles.

# Articles
1. Getting started with Spring AI **Chat Model** and easily switch between different AI providers including **OpenAI**, **Mistral AI** and **Ollama**. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Getting Started with Spring AI and Chat Model](https://piotrminkowski.com/2025/01/28/getting-started-with-spring-ai-and-chat-model)
2. Getting started with Spring AI **Function Calling** for OpenAI chat models. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Getting Started with Spring AI Function Calling](https://piotrminkowski.com/2025/01/30/getting-started-with-spring-ai-function-calling)
3. Using **RAG** (Retrieval Augmented Generation) and **Vector Store** with Spring AI. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Using RAG and Vector Store with Spring AI](https://piotrminkowski.com/2025/02/24/using-rag-and-vector-store-with-spring-ai/)
4. Using **Multimodality** feature and **Image Model** with Spring AI and OpenAI. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Spring AI with Multimodality and Images](https://piotrminkowski.com/2025/03/04/spring-ai-with-multimodality-and-images/)
5. Running multiple models with **Ollama** and integration through Spring AI. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Using Ollama with Spring AI](https://piotrminkowski.com/2025/03/10/using-ollama-with-spring-ai/)
6. Getting started with Spring AI **Tool Calling** for OpenAI/MistralAI chat models. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Tool Calling with Spring AI](https://piotrminkowski.com/2025/03/13/tool-calling-with-spring-ai/)
7. Integrate Spring AI with **Azure OpenAI** for chat models, image generation, tool calling and RAG. The example is available in the branch [master](https://github.com/piomin/spring-ai-showcase/tree/master). A detailed guide may be found in the following article: [Spring AI with Azure OpenAI](https://piotrminkowski.com/2025/03/25/spring-ai-with-azure-openai/)

