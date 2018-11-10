module.exports = function(grunt) {
    "use strict";
  
    grunt.initConfig({
      ts: {
        app: {
          files: [{
            src: ["src/\*\*/\*.ts", "!src/.baseDir.ts"],
            dest: "./dist"
          }],
          options: {
            module: "commonjs",
            target: "es6",
            sourceMap: false,
            experimentalDecorators: true,
            rootDir: "src"
          }
        }
      },
    });
  
    grunt.loadNpmTasks("grunt-ts");
  
    grunt.registerTask("default", [
      "ts"
    ]);
  
  };