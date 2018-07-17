var gulp = require('gulp'), // Подключаем Gulp
    less = require('gulp-less'), // Подключаем Less пакет
    browserSync = require('browser-sync'), // Подключаем Browser Sync
    autoprefixer = require('gulp-autoprefixer'), // Подключаем библиотеку для автоматического добавления префиксов
    cssnano = require('gulp-cssnano'), // Подключаем пакет для минификации CSS
    rename = require('gulp-rename'); // Подключаем библиотеку для переименования файлов;


gulp.task('less', function() { // Создаем таск less
    return gulp.src('less/index.less') // Берем источник
    .pipe(less())  // Преобразуем Less в CSS посредством gulp-less
    .pipe(autoprefixer({browsers: ['last 15 versions'],  cascade: true })) // Создаем префиксы
    .pipe(gulp.dest('css')) // Выгружаем результата в папку app/css
    .pipe(browserSync.reload({stream: true})) // Обновляем CSS на странице при изменении
});

gulp.task('css-minify-and-rename', function() { // Создаем таск css-minify-and-rename
    return gulp.src('css/index.css') // Выбираем файл для минификации
        .pipe(cssnano()) // Сжимаем
        .pipe(rename({suffix: '.min'})) // Добавляем суффикс .min
        .pipe(gulp.dest('css')) // Выгружаем в папку app/css
});


gulp.task('browser-sync', function() { // Создаем таск browser-sync
    browserSync({ // Выполняем browserSync
        server: { // Определяем параметры сервера
            baseDir: './' // Директория для сервера - app
        },
        notify: false // Отключаем уведомления
    });
});

gulp.task('watch', ['less', 'browser-sync'], function() { // Создаем таск watch
    gulp.watch('less/**/*.less', ['less']); // Наблюдение за less файлами в папке less
    gulp.watch('*.html', browserSync.reload); // Наблюдение за HTML файлами в корне проекта
});

gulp.task('default', ['watch']); // Дефолтный таск