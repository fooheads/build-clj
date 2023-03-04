# build-clj

This is fooheads build.clj, used in our libs and projects.

To use it, first create a file in you librarys root called lib.edn. 


```clojure
{:name com.fooheads/example-lib
 :version "1.3.2"
```

If you are to deploy it to clojars, the namespace of the symbol id should be your clojars group-id 
(`com.fooheads` in this example).

Then add this alias to your `deps.edn`:

```clojure
:build 
{:deps 
 {com.fooheads/build-clj {:mvn/version "1.0.0"}} 
  :ns-default fooheads.build}
```

That's it. Now you can do the following:

## clean

Cleans the `target` folder.

```
clojure -T:build clean
```

## jar

Calls `clean` and then builds a jar file with name (excluding namespace/group) and 
version from `lib.edn`

```
clojure -T:build jar
```

Example output:

```
Build folder "target" removed
Jar file created: "target/stdlib-0.1.12.jar"
```

## deploy

Deploys the lib to clojars. You need `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` as environment variables.

```
clojure -T:build deploy
```


