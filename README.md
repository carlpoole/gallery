# Gallery
A gallery app to load and display images from Lorem Picsum

<img src="https://i.imgur.com/kn4Mezf.png" width="240"> <img src="https://i.imgur.com/0yA5ADY.png" width="240"> <img src="https://i.imgur.com/P4IW9wU.png" width="240"> 

Written in Java

## Libs

- [RxAndroid](https://github.com/ReactiveX/RxAndroid): Android wrapper for RxJava for async events based observable pattern.
- [Retrofit](https://square.github.io/retrofit/): HTTP Library.
- [Gson](https://github.com/google/gson): Used for data serialization and modeling with Retrofit.
- [Parceler](https://github.com/johncarl81/parceler): Used for making custom data model objects more easily passed between activities.
- [Glide](https://bumptech.github.io/glide/): Image loading and caching library 
- [PhotoView](https://github.com/chrisbanes/PhotoView): Used for the full-screen image view to support pinch-zoom and panning.
- [SpinKit](https://github.com/ybq/Android-SpinKit): Pretty animations lib used for initial loading animation.

## Highlights

- When images are downloaded from Lorem Picsum, Glide caches them locally. This local cache is referenced before the app tries to re-download any images over the network again.

- [ViewModels](https://github.com/carlpoole/gallery/tree/master/app/src/main/java/codes/carl/gallery/model/views) are used to persist app data between configuration changes (screen rotation, split screen, etc.)

- The [sorting methods](https://github.com/carlpoole/gallery/blob/master/app/src/main/java/codes/carl/gallery/utils/SortUtils.java) (by Author name and by image area) are totally custom. This was done for the additional challenge. To avoid stack overflow problems, the sort methods use a stack structure to load and unload operations.
  - Author name sort is performed using a Trie structure to sort alphabetically. This works by creating a tree structure based on the letters in the name, and then running a pre-order depth-first-search to flatten the tree.
  - Image area sort uses Quicksort.
  - [Unit tests can be found for these sort methods here](https://github.com/carlpoole/gallery/tree/master/app/src/test/java/codes/carl/gallery).
  
- Tapping an image in the gallery loads it in a full-screen view where the image can be zoomed by using a pinch gesture, or panned by using two fingers.

- Tapping the bar with the author's name and (i) icon reveals a modal web view that takes you to the web page for the image to view more details or related images.
