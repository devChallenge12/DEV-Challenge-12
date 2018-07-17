'use strict';

var gulp = require('gulp');

// js variables
var uglify = require('gulp-uglify');
var jshint = require('gulp-jshint');
var concat = require('gulp-concat');

// scss variables
var sass = require('gulp-sass');

// html variables
var htmlmin = require('gulp-htmlmin');

// postcss variables
var postcss = require('gulp-postcss');
var sourcemaps = require('gulp-sourcemaps');
var autoprefixer = require('autoprefixer');
var cssnano = require('gulp-cssnano');

var paths = {
  css: ['src/css/**/*.css'],
  html: ['src/html/*.html'],
  js: ['src/js/*.js'],
  jsWatch: ['src/js/*.js', 'src/js/*.min.js'],
  scss: ['src/scss/**/*.scss']
};

gulp.task('scss', function () {
  return gulp.src(paths.scss)
    .pipe(sourcemaps.init())
    .pipe(sass().on('error', sass.logError))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('src/css'));
});

gulp.task('css:minify', function () {
  return gulp.src(paths.css)
    .pipe(cssnano())
    .pipe(postcss([autoprefixer({ browsers: ['ie >= 10', 'last 4 versions', '> 1%'] })]))
    .pipe(gulp.dest('dist/css'));
});

gulp.task('html:minify', function() {
  return gulp.src(paths.html)
    .pipe(htmlmin({collapseWhitespace: true}))
    .pipe(gulp.dest('dist'));
});

gulp.task('js', function () {
  return gulp.src(paths.jsWatch)
    .pipe(concat('main.js'))
    .pipe(jshint())
    .pipe(jshint.reporter('default'))
    .pipe(gulp.dest('src/js'));
});

gulp.task('js:minify', function () {
  return gulp.src(paths.js)
    .pipe(uglify())
    .pipe(gulp.dest('dist/js'));
});

gulp.task('watcher', function () {
  gulp.watch(paths.scss, ['scss']);
  gulp.watch(paths.jsWatch, ['js']);
});

gulp.task('build', ['css:minify', 'html:minify', 'js:minify'], function () {
  console.log('Build success');
});
