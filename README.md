# NasaApp

I have used MVVM architecture in the app along with retrofit with coroutines and repository to call the NASA API.
I have also used Dagger Hilt for dependency injection to inject repository and viewModelProviderFactory
to inject them in the Activity.

I have used a 3rd party library Glide to load images into ImageViews.
The packages are structured to clearly show the different architectural modules and classify all the files.

To show all the records fetched from the API, I have used pagination so when the recycler view is scrolled to the end,
new records of next page are fetched from the API.

