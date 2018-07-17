'use strict';

var gulp = require('gulp'),
    watch = require('gulp-watch'),
    less = require('gulp-less'),
    Cleanless = require('less-plugin-clean-css'),
    Autoprefix = require('less-plugin-autoprefix'),
    minifyjs = require('gulp-js-minify'),
    cleanCSS = require('gulp-clean-css'),
    rigger = require('gulp-rigger'),
    rimraf = require('rimraf'),
    browserSync = require("browser-sync"),
    reload = browserSync.reload;

var path = {
    build: {
        html: 'dist/',
        php: 'dist/',
        js: 'dist/assets/js/',
        css: 'dist/assets/css/',
        fonts: 'dist/assets/fonts/',
        img: 'dist/assets/media/img/',
        video: 'dist/assets/media/video/'
    },
    src: {
        html: 'src/html/*.html',
        php: 'src/php/*.php',
        js: 'src/assets/js/*.js',
        less: 'src/assets/less/**/*.less',
        css: 'src/assets/css/*.css',
        fonts: 'src/assets/fonts/**/*.*',
        img: 'src/assets/img/**/*.*',
        video: 'src/assets/video/**/*.*'
    },
    watch: {
        html: 'src/html/**/*.html',
        php: 'src/php/**/*.php',
        js: 'src/assets/js/**/*.js',
        less: 'src/assets/less/**/*.less',
        fonts: 'src/assets/fonts/**/*.*',
        img: 'src/assets/img/**/*.*',
        video: 'src/assets/video/*.*'
    },
    clean: './dist'
};

var config = {
    server: {
        baseDir: "./dist"
    },
    tunnel: true,
    host: 'localhost',
    port: 9000,
    logPrefix: "Frontend_:"
};

gulp.task('webserver', function () {
    browserSync(config);
});

gulp.task('clean', function (cb) {
    rimraf(path.clean, cb);
});

gulp.task('html:build', function () {
 gulp.src(path.src.html)
 .pipe(rigger())
 .pipe(gulp.dest(path.build.html))
 .pipe(reload({stream: true}));
 });

gulp.task('php:build', function () {
 gulp.src(path.src.php)
 .pipe(gulp.dest(path.build.php))
 .pipe(reload({stream: true}));
 });

gulp.task('js:build', function () {
    gulp.src(path.src.js)
        .pipe(rigger())
        .pipe(minifyjs())
        .pipe(gulp.dest(path.build.js))
        .pipe(reload({stream: true}));
});

gulp.task('less:build', function () {
    gulp.src(path.src.less)
        .pipe(less({
            plugins: [
                new Autoprefix({browsers: ["last 10 versions"]}),
                new Cleanless({advanced: true})
            ]
        }))
        .pipe(gulp.dest(path.build.css))
        .pipe(reload({stream: true}));
});

gulp.task('css:build', function () {
    gulp.src(path.src.css)
        .pipe(cleanCSS({compatibility: 'ie8'}))
        .pipe(gulp.dest(path.build.css))
        .pipe(reload({stream: true}));
});


gulp.task('image:build', function () {
    gulp.src(path.src.img)
    .pipe(gulp.dest(path.build.img))
    .pipe(reload({stream: true}));
});

gulp.task('fonts:build', function() {
    gulp.src(path.src.fonts)
        .pipe(gulp.dest(path.build.fonts))
});

gulp.task('video:build', function() {
    gulp.src(path.src.video)
        .pipe(gulp.dest(path.build.video))
});

gulp.task('build', [
    'html:build',
    'php:build',
    'js:build',
    'less:build',
    'css:build',
    'fonts:build',
    'image:build',
    'video:build'
]);

gulp.task('watch', function(){
    watch([path.watch.html], function(event, cb) {
        gulp.start('html:build');
     });
    watch([path.watch.php], function(event, cb) {
        gulp.start('php:build');
     });
    watch([path.watch.less], function(event, cb) {
        gulp.start('less:build');
    });

    watch([path.watch.js], function(event, cb) {
        gulp.start('js:build');
    });

    watch([path.watch.img], function(event, cb) {
        gulp.start('image:build');
    });
	
    watch([path.watch.video], function(event, cb) {
        gulp.start('video:build');
    });
    watch([path.watch.fonts], function(event, cb) {
        gulp.start('fonts:build');
    });
});

gulp.task('default', ['build', 'webserver', 'watch']);
