# Преобразование набора данных магазина Google Play из CSV в JSON  
Парсит собранные из магазина Google Play сырые данные о приложениях из формата CSV в JSON.  
Группирует приложения по тематикам (PRODUCTIVITY, GAME, TOOLS и пр.), категории переведены на русский.  
Также есть вывод просто в txt-файл.  

Пока не нашла решения проблемы с переводом Date в ISO8601, поэтому в поле lastUpdated хранится просто Date.  
Видела решение подобного на Java, но пока у меня на Kotlin выдаёт ошибки.
