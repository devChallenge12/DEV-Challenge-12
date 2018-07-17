var gulp = require('gulp'),
    sass = require('gulp-sass'),
    autoprefixer = require('gulp-autoprefixer'),
    rename = require('gulp-rename'),
    uglify = require('gulp-uglify'),
    cleanCSS = require('gulp-clean-css'),
    watch = require('gulp-watch'),
    rimraf = require('gulp-rimraf'),
    imagemin = require('gulp-imagemin'),
		pngquant = require('imagemin-pngquant'),
		cache = require('gulp-cache'),
    svgstore = require('gulp-svgstore'),
    svgmin = require('gulp-svgmin'),
    cheerio = require('gulp-cheerio'),
		replace = require('gulp-replace'),
    babelify = require('babelify'),
    browserify = require('browserify'),
    vueify = require('vueify'),
    source = require('vinyl-source-stream'),
    browserSync = require("browser-sync"),
    reload = browserSync.reload;

gulp.task('serve', function () {
	setTimeout(function(){
		browserSync({
      server: {
        baseDir: "./dist"
      },
      files: ['./dist/**/*'],
      tunnel: false,
      host: 'localhost',
      port: 9000,
      logPrefix: "frontend",
      watchTask: true,
      notify: false
    });
	}, 5000);
});

gulp.task('html', function () {
	return gulp.src(['src/*.html', 'src/*.json'])
	.pipe(gulp.dest('dist/'))
	.pipe(reload({stream: true}));
});

gulp.task('js', function() {
  return browserify({ entries: 'src/main.js'})
    .transform(babelify, { presets: ['es2015'] })
    .transform(vueify)
    .plugin('vueify/plugins/extract-css', {
      out: 'src/scss/_bundle.css' // can also be a WritableStream
    })
    .bundle()
      .pipe(source('main.js'))
    	// .pipe(uglify())
      // .pipe(rename({suffix: '.min'}))
      .pipe(gulp.dest('dist/js'))
      .pipe(reload({stream: true}))
});

gulp.task('scss', function() {
	return gulp.src('src/scss/main.scss')
	.pipe(sass().on('error', sass.logError))
  .pipe(rename({suffix: '.min'}))
  .pipe(autoprefixer({
      browsers: ['last 2 versions'],
      cascade: false
  }))
  .pipe(cleanCSS())
	.pipe(gulp.dest('dist/css'))
	.pipe(reload({stream: true}))
});

gulp.task('fonts', function() {
	return gulp.src('src/fonts/**/*.*')
	.pipe(gulp.dest('dist/fonts/'))
});

gulp.task('img', function() {
	return gulp.src(['src/img/**/*', '!src/img/svg'])
		.pipe(cache(imagemin({
			interlaced: true,
			progressive: true,
			svgoPlugins: [{removeViewBox: false}],
			use: [pngquant()]
		})))
		.pipe(gulp.dest('dist/img'));
});

gulp.task('svg', function () {
    return gulp
        .src('src/img/svg/*.svg')
        .pipe(svgmin({
          js2svg: {
            pretty: true
          }
        }))
        .pipe(cheerio({
          run: function ($) {
            $('[fill]').removeAttr('fill');
            $('[style]').removeAttr('style');
          },
          parserOptions: { xmlMode: true }
        }))
        .pipe(replace('&gt;', '>'))
        .pipe(svgstore())
        .pipe(gulp.dest('src/img/'));
});

gulp.task('build', [
	'html',
	'scss',
	'js',
  'fonts',
  'img'
	]);

gulp.task('watch', function(){
	watch(['src/*.html'], function(event, cb) {
		gulp.start('html');
	});
	watch(['src/scss/**/*.scss', 'src/scss/**/*.css'], function(event, cb) {
		gulp.start('scss');
	});
	watch(['src/components/**/*.vue', 'src/main.js'], function(event, cb) {
		gulp.start('js');
	});
  watch(['src/img/**/*.*', '!src/img/svg'], function(event, cb) {
		gulp.start('img');
	});
});

gulp.task('clean', function () {
	return gulp.src('dist/*')
	.pipe(rimraf())
});

gulp.task('svgstore', function () {
    return gulp
        .src('src/img/svg/*.svg')
        .pipe(svgmin({
          js2svg: {
            pretty: true
          }
        }))
        .pipe(cheerio({
          run: function ($) {
            $('[fill]').removeAttr('fill');
            $('[style]').removeAttr('style');
          },
          parserOptions: { xmlMode: true }
        }))
        .pipe(replace('&gt;', '>'))
        .pipe(svgstore())
        .pipe(gulp.dest('src/img/'));
});

gulp.task('default', ['build', 'serve', 'watch']);
