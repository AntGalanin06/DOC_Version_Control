# 📚 Лабораторные работы по курсу «Шаблоны проектирования программного обеспечения»

В этом репозитории мы последовательно выполняем три лабораторные работы.

---

## 🔖 ЛР 1 (Java + Swing)
**Тема:** «Модель документа с полной историей изменений»  
**Технологии:** Java 23 + Swing  
**Паттерны:** ≥ 6 классических GoF (Factory Method, Singleton, Decorator, Command, Memento, Observer)

**Краткое содержание:**
1. **Document** + три типа (`Text`, `PDF`, `Spreadsheet`) через Factory Method
2. История изменений с Undo/Redo/Jump (Memento + Caretaker)
3. Команды для действий пользователя (Command + Invoker)
4. Расширения через Decorator: шифрование содержимого и логирование операций (Singleton для сбора логов)
5. Автоматическое обновление интерфейса и консоли (Observer)

👉 [Релиз ЛР 1 → v1-lab-patterns](https://github.com/AntGalanin06/DOC_Version_Control/releases/tag/v1-lab-patterns)  
📥 [Скачать отчёт ЛР 1 (DOCX)](https://github.com/AntGalanin06/DOC_Version_Control/raw/master/docs/ЛР_1.docx)

---

## 🔖 ЛР 2 (Spring + AOP)
**Тема:** «IoC и DI средствами Spring Framework. AOP»  
**Технологии:** Java 23 + Spring Framework 6.2.6  
**Паттерны:** IoC/DI, AOP, Factory Method, Decorator, Memento, Observer, Command

**Краткое содержание:**
1. **Spring IoC/DI**  
   — все инфраструктурные компоненты (фабрики, GUI, декораторы, логгер истории, аспект) объявлены как Spring Bean
2. **Внедрение зависимостей (DI)**  
   — через конструкторы и `ObjectProvider`  
   — бизнес-классы (документы, команды) остаются чистыми POJO
3. **AOP-логирование**  
   — аннотация `@Loggable` + `LoggingAspect`  
   — автоматический сбор CALL/RETURN/ERROR
4. **Контекст Spring**  
   — создаётся и закрывается в `main()`, остальной код работает через DI

👉 [Релиз ЛР 2 → v2-lab-spring](https://github.com/AntGalanin06/DOC_Version_Control/releases/tag/v2-lab-spring)  
📥 [Скачать отчёт ЛР 2 (DOCX)](https://github.com/AntGalanin06/DOC_Version_Control/raw/master/docs/ЛР_2.docx)

---

## 🔖 ЛР 3 (Concurrency)
**Тема:** «Основы многопоточности в Java»  
**Технологии:** Java 23 + Swing + Concurrency

**Краткое содержание:**
1. **Producer–Consumer** (генерация запросов → очередь → исполнители)
2. **ClientGenerator** — настраиваемая генерация burst/jitter
3. **RequestQueue** — `LinkedBlockingQueue<Request>`
4. **WorkerPool** и `Worker` — выполнение команд над `Document`
5. **StatsAggregator** (`ClientStat`, `WorkerStat`) — сбор задержек, throughput, busy/idle
6. **SystemMonitorGUI** — вкладки `Queue` и `Logs` для мониторинга
7. **Graceful shutdown** — poison-pill `SHUTDOWN`, остановка потоков

👉 [Релиз ЛР 3 → v3-lab-multithreading](https://github.com/AntGalanin06/DOC_Version_Control/releases/tag/v3-lab-multithreading)  
📥 [Скачать отчёт ЛР 3 (DOCX)](https://github.com/AntGalanin06/DOC_Version_Control/raw/master/docs/ЛР_3.docx)
