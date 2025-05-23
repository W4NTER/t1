![Build](https://github.com/W4NTER/t1/actions/workflows/t1.yml/badge.svg)

# Учебный проект открытых школ T1

## Описание

Этот проект включает в себя несколько ключевых компонентов:

- **Кастомный Spring Boot стартер** для логирования и обработки API-запросов и ответов.
- **Аспекты** для логирования действий с помощью `@LogExecution`, `@LogException`, `@HandlingResult`, и `@LogTracking` (работа с аспектами вынесена в [starter_t1](https://github.com/W4NTER/starter_t1)).
- **Интеграция с Kafka** для обработки сообщений.

Проект демонстрирует использование аспектно-ориентированного программирования в Spring, реализацию собственного Spring Boot стартера, интеграцию с Kafka, а также организацию тестового покрытия кода.

## Технологии

- **Java** (JDK 21)
- **Spring Boot**
- **Kafka**
- **Custom Spring Boot Starter** ([starter_t1](https://github.com/W4NTER/starter_t1))
- **AOP** для логирования
- **JUnit** для тестирования
- **Docker** для контейнеризации


## Интеграция с Codecov

В проекте настроен сервис для анализа покрытия тестами с помощью [Codecov](https://codecov.io/), который отслеживает качество тестов и покрытие кода.

[![codecov](https://codecov.io/gh/W4NTER/t1/branch/main/graph/badge.svg)](https://codecov.io/gh/W4NTER/t1)

## CI/CD

Проект использует [GitHub Actions](https://github.com/W4NTER/t1/actions), чтобы автоматизировать процесс тестирования и сборки. После каждого изменения в коде запускаются тесты, и автоматически проверяется покрытие тестами.

## Кастомный шильдик с покрытием

Здесь я просто распарсил отчет jacoco и через actions пушу изменения о покрытии в README.

<!-- COVERAGE_BADGE --> ![Coverage](https://img.shields.io/badge/Coverage-88.17%25-brightgreen.svg)

P.S. Очень странный и достаточно плохой способ отображать покрытие в README (Да и в целом нужно для начала ответить на вопрос "Зачем?"), но мне хотелось реализовать это без интеграции со сторонними сервисами.
