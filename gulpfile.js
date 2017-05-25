const gulp = require('gulp')
	child = require('child_process')
	gutil = require('gulp-util')
	browserSync = require('browser-sync').create();

const config = {
	siteRoot: './docs',
	siteSrc: './src/**/*'
}

gulp.task('webpack', () => {
  const webpack = child.spawn('webpack');

  const webpackLogger = (buffer) => {
    buffer.toString()
      .split(/\n/)
      .forEach((message) => gutil.log('Webpack: ' + message));
  };

  webpack.stdout.on('data', webpackLogger);
  webpack.stderr.on('data', webpackLogger);
});

gulp.task('serve', () => {
  browserSync.init({
    files: [config.siteRoot + '/**'],
    port: 4000,
    server: {
      baseDir: config.siteRoot
    }
  });
  
  gulp.watch(config.siteSrc, ['webpack']);
});

gulp.task('default', ['webpack', 'serve']);