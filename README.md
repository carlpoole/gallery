# Gallery
A gallery app to load and display images from Lorem Picsum. The source data for the app is https://picsum.photos/v2/list

<img src="https://i.imgur.com/kn4Mezf.png" width="240"> <img src="https://i.imgur.com/0yA5ADY.png" width="240"> <img src="https://i.imgur.com/ZJOpmYH.png" width="240"> 

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

- When images are downloaded from Lorem Picsum, Glide caches them locally to memory and disk. This local cache is referenced before the app tries to re-download any images over the network again.

- The gallery view can be refreshed from Lorem Picsum by pulling down at the top of the gallery list. Previously selected sort options apply.

- [ViewModels](https://github.com/carlpoole/gallery/tree/master/app/src/main/java/codes/carl/gallery/model/views) are used to persist app data between configuration changes (screen rotation, split screen, etc.)

- The [sorting methods](https://github.com/carlpoole/gallery/blob/master/app/src/main/java/codes/carl/gallery/utils/SortUtils.java) (by Author name and by image area) are totally custom. This was done for the additional challenge. To avoid stack overflow problems, the sort methods use a stack structure to load and unload operations.
  - Author name sort is performed using a Trie structure to sort alphabetically. This works by creating a tree structure based on the letters in the name, and then running a pre-order depth-first-search to flatten the tree. Non English characters are ignored for the sake of this exercise. (Images with author names consisting of entirely non-english or illegal characters will appear at the front of the gallery).
  - Image area sort uses Quicksort. The images display in smallest to largest area.
  - [Unit tests can be found for these sort methods here](https://github.com/carlpoole/gallery/tree/master/app/src/test/java/codes/carl/gallery).
  
- Tapping an image in the gallery loads it in a full-screen view where the image can be zoomed by using a pinch gesture, or panned by using two fingers.

- Tapping the bar with the author's name and (i) icon reveals a modal web view that takes you to the web page for the image to view more details or related images.
  - Both the back button and Android back button will first navigate back in the web view. If there are no more pages to go back to, the web info modal will then close.
  - You can also tap outside the modal window to dismiss it at any time.
