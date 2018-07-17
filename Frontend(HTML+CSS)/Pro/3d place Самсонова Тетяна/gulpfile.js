var gulp       = require('gulp'), // Подключаем Gulp
	sass         = require('gulp-sass'), //Подключаем Sass пакет,
	browserSync  = require('browser-sync'), // Подключаем Browser Sync
	concat       = require('gulp-concat'), // Подключаем gulp-concat (для конкатенации файлов)
	/*uglify       = require('gulp-uglifyjs'),*/ // Подключаем gulp-uglifyjs (для сжатия JS)
	/*cssnano      = require('gulp-cssnano'),*/ // Подключаем пакет для минификации CSS
	rename       = require('gulp-rename'), // Подключаем библиотеку для переименования файлов
	del          = require('del'), // Подключаем библиотеку для удаления файлов и папок
	imagemin     = require('gulp-imagemin'), // Подключаем библиотеку для работы с изображениями
	pngquant     = require('imagemin-pngquant'), // Подключаем библиотеку для работы с png
	cache        = require('gulp-cache'), // Подключаем библиотеку кеширования
	plumber = require('gulp-plumber'), // Чтоб при ошибке не падал сервер
	autoprefixer = require('gulp-autoprefixer');// Подключаем библиотеку для автоматического добавления префиксов
	spritesmith = require('gulp.spritesmith'); //Для автосборки спрайта
	sourcemaps = require('gulp-sourcemaps'); //Что б в режиме разработчика показывало норм стили

gulp.task('sass', function(){ // Создаем таск Sass
	return gulp.src('src/scss/**/*.scss') // Берем источник
		.pipe(sourcemaps.init())
		.pipe(plumber())
		.pipe(sass({outputStyle: 'compact'}).on('error', sass.logError)) // Преобразуем Sass в CSS посредством gulp-sass				
		.pipe(autoprefixer(['last 10 versions', '> 1%', 'ie 9', 'ie 10'], { cascade: true })) // Создаем префиксы
		.pipe(sourcemaps.write('.'))
		.pipe(gulp.dest('src/css')) // Выгружаем результата в папку src/css
		.pipe(browserSync.reload({stream: true})) // Обновляем CSS на странице при изменении
});





gulp.task('browser-sync', function() { // Создаем таск browser-sync
	browserSync({ // Выполняем browserSync
		server: { // Определяем параметры сервера
			baseDir: 'src' // Директория для сервера - src
		},
		notify: false // Отключаем уведомления
	});
});

gulp.task('sprite', function () {
  var sprite= gulp.src('src/img/icons/*.png').pipe(spritesmith({
    imgName: '../img/sprite.png',
    cssName: '_sprite.scss',
    cssFormat: 'scss',
    algoritm: 'binary-tree',
    padding: 5
  }));
  sprite.img.pipe(rename('sprite.png')).pipe(gulp.dest('src/img/'));
  sprite.css.pipe(gulp.dest('src/scss/utils/'));
});
/* если нужно обединить все скрипты в один розкоментировать и в шаблоне подключить один файл*/
/*gulp.task('scripts', function() {
	return gulp.src([*/
		/*'src/js/jquery-3.0.0.min.js',
		'src/js/jquery-migrate-1.4.1.min.js',
		'src/js/components/jquery.fancybox.js',
		'src/js/components/jquery.formstyler.js',
		'src/js/components/jquery.mCustomScrollbar.js',
		'src/js/components/slick.js'*///тут нужно подключать библиотеки
		/*])
	.pipe(concat('scripts.min.js'))
	.pipe(uglify())
	.pipe(gulp.dest('src/js'))
	.pipe(browserSync.reload({stream: true}));
});*/


gulp.task('watch', ['browser-sync', 'sprite'], function() {
	gulp.watch('src/scss/**/*.scss', ['sass']); // Наблюдение за sass файлами в папке sass
	gulp.watch(['src/img/icons/**/*.png'], ['sprite']);
	gulp.watch('src/*.html', browserSync.reload); // Наблюдение за HTML файлами в корне проекта
	gulp.watch('src/js/**/*.js', browserSync.reload);   // Наблюдение за JS файлами в папке js
});

gulp.task('clean', function() {
	return del.sync('dist'); // Удаляем папку dist перед сборкой
});

gulp.task('img', function() {
	return gulp.src('src/img/**/*') // Берем все изображения из src
		.pipe(cache(imagemin({  // Сжимаем их с наилучшими настройками с учетом кеширования
			interlaced: true,
			progressive: true,
			svgoPlugins: [{removeViewBox: false}],
			use: [pngquant()]
		})))
		.pipe(gulp.dest('dist/img')); // Выгружаем на продакшен
});

gulp.task('build', ['clean', 'img', 'sass'], function() {

	var buildCss = gulp.src([ // Переносим библиотеки в продакшен
		'src/css/style.css'
		])
	.pipe(gulp.dest('dist/css'))

	var buildFonts = gulp.src('src/scss/**/*') // Переносим scss в продакшен
	.pipe(gulp.dest('dist/scss'))

	var buildFonts = gulp.src('src/fonts/**/*') // Переносим шрифты в продакшен
	.pipe(gulp.dest('dist/fonts'))

	var buildJs = gulp.src('src/js/**/*') // Переносим скрипты в продакшен
	.pipe(gulp.dest('dist/js'))

	var buildHtml = gulp.src('src/*.html') // Переносим HTML в продакшен
	.pipe(gulp.dest('dist'));
	
	var buildJson = gulp.src('src/data/**/*') // Переносим скрипты в продакшен
	.pipe(gulp.dest('dist/data'))

});

gulp.task('clear', function (callback) {
	return cache.clearAll();
})

gulp.task('default', ['watch']);
