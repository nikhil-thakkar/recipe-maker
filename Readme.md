# About

The purpose of this repo to demonstrate the use of TDD, Koin, dynamic feature modules and everything JetPack :muscle:.

This app is leveraging the [EventBrite API's](https://www.eventbrite.com/platform/docs/introduction) to build a clone of EventBrite itself :v:. Just another client :smile: for education purposes.

# The Setup

Currently the application has three feature (supposedly dynamic) modules namely *home*, *onboarding*, *search*. This may change in the future.

As a defacto, the application has three other modules namely *app* (I bet you guessed it :wink:), *core* (maybe this as well :thumbsup:), and *test_shared* (from [plaid](https://github.com/android/plaid))).

The application uses a centralized dependency management logic defined inside `buildSrc` and its own **Plugin** to be applied to feature modules. Also, this has been extracted out to an independent repo [here](https://github.com/nikhil-thakkar/multi-module-dependency-setup) if you want it to use in your own app :smile:.

This repo tries to combine the learnings from across different blog posts/repos on individual topics and as best effort to apply them here. Mobile development is becoming hard :ghost: (bahut hard).

# References
* [JetPack](https://developer.android.com/jetpack)
* [Android Github](https://github.com/android)

