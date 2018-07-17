var gulp = require('gulp');
var autoPrefixer = require('gulp-autoprefixer');
var cleanCSS = require('gulp-clean-css');
var sass = require('gulp-sass');
var server = require('gulp-server-livereload');
var imagemin = require('gulp-imagemin');

var page = 'index';

var paths = {
    src: {
        pages: '*.html',
        images: 'img/*',
        fonts: 'fonts/**/*',
        scss: `scss/${page}.scss`,
        webserver: '../dist'
    },
    dest: {
        pages: '../dist',
        images: '../dist/img',
        fonts: '../dist/fonts',
        scss: '../dist/css'
    },
    watch: {
        html: '**/*.html',
        img: 'img/*',
        fonts: 'fonts/**/*',
        scss: 'scss/**/*.scss'
    }
};

gulp.task('pages', function(){
    return gulp.src(paths.src.pages)
        .pipe(gulp.dest(paths.dest.pages))
});

gulp.task('images', function(){
    return gulp.src(paths.src.images)
        .pipe(imagemin({optimizationLevel: 5}))
        .pipe(gulp.dest(paths.dest.images))
});

gulp.task('fonts', function(){
    return gulp.src(paths.src.fonts)
        .pipe(gulp.dest(paths.dest.fonts))
});

gulp.task('sass', function(done) {
     gulp.src(paths.src.scss)
        .pipe(sass({ sourcemap: true, style: 'compact' }).on('error', sass.logError))
        .pipe(autoPrefixer('last 2 version'))
        .pipe(cleanCSS())
        .pipe(gulp.dest(paths.dest.scss));
    done();
});

gulp.task('webserver', function() {
    return gulp.src(paths.src.webserver)
        .pipe(server({
            livereload: {
                enable: true,
                filter: function(filePath, cb) {
                    cb( !(/.DS_Store/.test(filePath)) );
                }
            },
            directoryListing: false,
            open: true,
            log: 'info',
            defaultFile: `${page}.html`
        }));
});

gulp.task('watchify', function(done) {
    gulp.watch('**/*.html', ['pages']);
    gulp.watch('img/*', ['images']);
    gulp.watch('fonts/**/*', ['fonts']);
    gulp.watch('scss/**/*.scss', ['sass']);
    done();
});

gulp.task('default', ['pages', 'sass', 'fonts', 'images', 'webserver', 'watchify']);