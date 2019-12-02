# WireLens

![AndroidDevChallenge Header](./assets/AndroidDevChallengeHeader.png)

## The Idea

It happens to all of us. We arrive at a new place, want to get online and are pleased to see they offer free WiFi. They have helpfully printed up the WiFi details for us, something like this:

![WiFi Signs](./assets/wifi-examples.png)

The problem is that it’s not always convenient to stand there, bring up your phone's WiFi settings and carefully type in the password. So you take a picture of the details then sit down to sort it out. But how do you view the image while entering the password? I usually end up trying to memorize chunks of it, switching between the image and the WiFi settings. Not ideal to say the least!

What if you could take a photo, and by the time you sat down, you were already connected to the network? This is what WireLens aims to provide. It uses on-device OCR and machine learning to use the photo combined with info about the WiFi networks available to establish the credentials we need and connect automatically. For times when OCR fails, such as when the details are handwritten, WireLens provides an interface that will show you the image overlaid by a text box where you can enter the password manually. These human-provided details can act as inputs to improve the ML component. Once the user has connected to the network they can tap to show a QR code so others can connect.

So, in the ideal case, WireLens seamlessly connects you after simply taking a photo. In the worst case, it provides a solution to the problem of how to manually enter details from a photograph with the possibility of using this as an input to improve the machine learning component of the app.

## Bringing it to Life

This is an idea that I started working on in the beginning of 2018 but came up short when I learned that on-device OCR (using Tesseract) was just not up to the task. Now, with MLKit proving so capable, I’m hoping to finally bring the idea to life. It’s an app I constantly find myself wanting as a user which I find is one of the best indicators that you’re building something worth making!

### Sample Code
Feel free to explore the code base which is a working (if not yet terribly functional) Android app. 
A few highlights:

* The app needs to search for common words like "Network" and "Password" and recognise them even if they come through as "Ne7w0rK" or "P4S5w0rd". Fuzzy text matching with specific attention to the peculiarities of OCR is therefore a must. The [FuzzyTextUtils](app/src/main/java/com/elroid/wirelens/util/FuzzyTextUtils.kt) class attempts to do just this by searching for different combinations of commonly confused characters.

* Combining the OCR results with an awareness of the WiFi landscape in a performant way requires some complicated logic. The [ConnectionGuesser](app/src/main/java/com/elroid/wirelens/domain/ConnectionGuesser.java) class is my attempt at making a fully testable process that will ensure that a successful connection happens as quickly as possible.

* For something like this with a complicated rule-base it's important to make sure you don't regress as you add support for different inputs. Therefore I decided test-driven development was crucial, with [unit tests](/app/src/test/kotlin/com/elroid/wirelens/utils), [business logic tests](app/src/test/kotlin/com/elroid/wirelens/domain) and [UI tests](app/src/androidTest/kotlin/com/elroid/wirelens/test/ui) prioritised.

### Ways Google can help
* I am initially going down the route of fuzzy text matching and rule-based parsing but I would like to see how I can leverage machine learning techniques to improve recognition. Better recognition of hand-written passwords would be invaluable. It also occurs to me that there are certain patterns that recur in these passwords - such as the habit of creating passwords using words with certain letters replaced by numbers. I’d like to see how machine learning of the structure of passwords could be used to improve password recognition.
* With best practice for interacting with WiFi undergoing big changes on the Android platform, I could use advice on how best to use the new tools to connect and/or suggest WiFi network credentials on current and past platforms.

### Timeline
I aim to put out one release each month:

#### 0.5 - MVP (3 January 2020)

* Allows import from camera or gallery with basic image parsing
* Adds guessed connection as a suggested network (Android Q/10+) 
* Basic text output of status (implemented as Foreground Service)

#### 0.6 - Refinement (7 February 2020)
	
* Manual entry screen showing image overlaid with a text box for manual entry
* Shows connection attempts as a list with button to bring up manual entry
* Adds direct connect to WiFi for devices running N/9 and below
* Alpha release on Google Play

#### 0.7 - Add QR code capabilities (6 March 2020)
	
* Adds list of successful WiFi logins with QR-code display
* Add QR code reading function (compatible with Android10 platform QR codes) 
* Continue to refine rules for different configurations of input 

#### 0.8 - Add ML (3 April 2020)
	
* Start on ML models	
* Add cloud storage of credential images and results for model training
* Refinement of UI, landscape support etc
* Public beta release on Google Play

#### 1.0 - Public Release (1 May 2020)
	
* Add on-boarding pages and UI polish
* Creation of final launcher icon
* Testing on various devices
* Full release on Google Play

#### Future
* Distribution of updated ML models to users for offline use
* Monetisation via mobile advertising with donation IAP to remove 
* Investigate the possibility of bringing WireLens to iOS via Kotlin MP

## About Me
I grew up in Silicon Valley but moved to the UK in my early 20s. I started out doing websites with Java but got the mobile dev bug back in 2005. I began with apps for J2ME/Symbian & Blackberry and for the past 10 years I've been creating Android apps as a consultant. While I enjoy the variety, I do like the idea of seeing a project through its entire lifecycle so I greatly value the opportunity to create something of my own. I’ve yet to manage to acquire entry to Google IO but would very much appreciate the chance to go back to my homeland to attend. :-)

More about me and projects I've been involved with: [elroid.com/cv](http://elroid.com/cv/)
