# Git Flow для Bookshop Backend

## 🌳 Структура веток

### Основные ветки:
- **`master`** - стабильная ветка с релизами (только для релизов)
- **`develop`** - ветка разработки (основная ветка для разработки)

### Вспомогательные ветки:
- **`feature/*`** - ветки для новых функций
- **`release/*`** - ветки для подготовки релизов
- **`hotfix/*`** - ветки для критических исправлений

## 🚀 Процесс разработки

### 1. Создание новой функции (Feature)

```bash
# Переключиться на develop
git checkout develop
git pull origin develop

# Создать ветку для новой функции
git checkout -b feature/название-функции

# Разработать функцию, делать коммиты
git add .
git commit -m "feat: описание изменения"

# Отправить ветку в удаленный репозиторий
git push -u origin feature/название-функции

# Создать Pull Request в GitHub для слияния в develop
```

### 2. Завершение функции

```bash
# Переключиться на develop
git checkout develop
git pull origin develop

# Слить feature ветку
git merge --no-ff feature/название-функции

# Удалить feature ветку
git branch -d feature/название-функции
git push origin --delete feature/название-функции
```

### 3. Подготовка релиза (Release)

```bash
# Переключиться на develop
git checkout develop
git pull origin develop

# Создать release ветку
git checkout -b release/1.2.0

# Обновить версию в pom.xml (убрать -SNAPSHOT)
# Сделать коммит
git add pom.xml
git commit -m "chore: prepare release 1.2.0"

# Отправить release ветку
git push -u origin release/1.2.0

# Создать Pull Request для слияния в master
```

### 4. Завершение релиза

```bash
# Переключиться на master
git checkout master
git pull origin master

# Слить release ветку
git merge --no-ff release/1.2.0

# Создать тег релиза
git tag -a v1.2.0 -m "Release version 1.2.0"

# Отправить тег
git push origin v1.2.0

# Переключиться на develop
git checkout develop
git merge --no-ff release/1.2.0

# Обновить версию в develop (добавить -SNAPSHOT)
# Сделать коммит и отправить
git add pom.xml
git commit -m "chore: bump version to 1.3.0-SNAPSHOT"
git push origin develop

# Удалить release ветку
git branch -d release/1.2.0
git push origin --delete release/1.2.0
```

### 5. Критические исправления (Hotfix)

```bash
# Переключиться на master
git checkout master
git pull origin master

# Создать hotfix ветку
git checkout -b hotfix/1.2.1

# Исправить критическую ошибку
# Сделать коммиты
git add .
git commit -m "fix: описание исправления"

# Отправить hotfix ветку
git push -u origin hotfix/1.2.1

# Создать Pull Request для слияния в master
```

### 6. Завершение hotfix

```bash
# Переключиться на master
git checkout master
git pull origin master

# Слить hotfix ветку
git merge --no-ff hotfix/1.2.1

# Создать тег релиза
git tag -a v1.2.1 -m "Hotfix version 1.2.1"

# Отправить тег
git push origin v1.2.1

# Переключиться на develop
git checkout develop
git merge --no-ff hotfix/1.2.1

# Отправить изменения
git push origin develop

# Удалить hotfix ветку
git branch -d hotfix/1.2.1
git push origin --delete hotfix/1.2.1
```

## 📋 Правила именования

### Ветки:
- `feature/название-функции` - для новых функций
- `release/версия` - для подготовки релизов
- `hotfix/версия` - для критических исправлений

### Коммиты:
- `feat:` - новая функция
- `fix:` - исправление ошибки
- `docs:` - документация
- `style:` - форматирование кода
- `refactor:` - рефакторинг
- `test:` - тесты
- `chore:` - обновление зависимостей, конфигурации

### Теги:
- `v1.0.0` - семантическое версионирование (MAJOR.MINOR.PATCH)

## 🔒 Защита веток

### Master ветка:
- Только через Pull Request
- Требуется ревью кода
- Только из release/hotfix веток

### Develop ветка:
- Только через Pull Request
- Требуется ревью кода
- Только из feature веток

## 🎯 Текущее состояние

- **Master**: v1.0.0 (стабильная версия)
- **Develop**: 1.1.0-SNAPSHOT (разработка)
- **Следующий релиз**: v1.1.0

## 📚 Полезные команды

```bash
# Посмотреть все ветки
git branch -a

# Посмотреть теги
git tag --list

# Посмотреть историю
git log --oneline --graph --all

# Синхронизировать с удаленным репозиторием
git fetch --all
git pull origin develop
```
