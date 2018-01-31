# Orange Engine

This is a 2D and 3D game engine I made back in highschool when I got my first Android phone. It is written in 100% Java, uses OpenGL ES 1.x (so no shaders), and is surprisingly functional. It weighs in at around ~30k LOC.


It is essentially the engine that powered my first Android game, SpinShip. I pulled out the core engine features and packaged it as a separate reusable engine after I released the game. Unfortunately, I didn't use any version control back in ~2010/2011 when I began development (I backed up my code to DVDs lol), so there's no development history from that point. I also, predictably, lost the source code for the original SpinShip :(


This engine was used to create the following apps that were/are published on the Android Market/Play Store, a few random non-Google app stores for Android, and Blackberry AppWorld:

* SpinShip, SpinShip Lite, SpinShip HD
* Crazy Virtual Dice 3D
* Paperflight
* Outpost
* Pixel^3 3d modeling tool
* Spiker Roller Live Wallpaper
* Pitch Speed

It was also used for a handful of apps I made for clients as a freelancer, and a bunch of incomplete personal projects. Pretty much the only thing that kept development of this engine alive was one giant project in particular that I ended up scrapping as well.

# Features\*

\*That I remember...

* 2D and 3D graphics
* API strongly inspired by the Irrlicht3D engine (it was my favorite at the time!)
* Works on pretty much every Android version ever (uses basic opengl-es 1.x features only)
* OBJ file format loading
* Full 2D scene graph with many different 'nodes'
 * sprite/spritesheet, animation, text, etc
* Virtual input system
 * buttons, joystick, toggle widgets, drag widgets
* Basic component-based entity system


# Usage

I don't really remember... I think I made a tutorial once for my brother because I was going to try to teach him programming using my engine, but that's probably lost. Best case is to look over some sample apps if you actually want to use this thing.


I know the API was strongly inspired by the Irrlicht 3D engine. There is an `OrangeDevice` class which is used to initialie everything, and you access the 2D and 3D features using `OSceneManager` for creating/drawing 3D `OSceneNode`s, and `OOrthoEnvironment` for `ONode2D`s
