# Orange Engine

This is a 2D and 3D game engine I made back in highschool when I got my first Android phone. It is written in 100% Java, uses OpenGL ES 1.x (so no shaders...I started making this thing on a HTC Dream/T-Mobile G1!), and is surprisingly functional.

[More information here](https://aramallo.com/projects/orange-engine)

# Why am I releasing this?

You know Unity and Unreal Engine 4? I'm going to steal their market share.

# How to use it

**Step 1: don't.**

But if you're *really* curious and want to dig through this mess, you'll have a functional, lightweight engine that works on pretty much every version of Android ever (no one *actually* uses anything older than Android 1.0). You'll also be able to constantly worry that everything will break because you're using an engine written by a kid who was using DVDs to backup his code when git was popular and widespread.

### Instructions

I don't entirely remember... I think I made a tutorial once for my brother because I was going to try to teach him programming using my engine, but that's probably lost. Best case is to look over the source code of [Crazy Virtual Dice 3D](https://github.com/AlexRamallo/virtual-dice) if you actually want to use this thing. [This function](https://github.com/AlexRamallo/virtual-dice/blob/master/src/com/snakeinalake/virtualdice/DiceRenderer.java#L91) in particular might be useful.


I know the API was strongly inspired by the Irrlicht 3D engine. There is an `OrangeDevice` class which is used to initialize everything, and you access the 2D and 3D features using `OSceneManager` for creating/drawing 3D `OSceneNode`s, and `OOrthoEnvironment` for `ONode2D`s

The `COglRenderer` implements really basic stuff regarding the lifecycle of the app, issuing the drawing commands, setting GL features and updating the device with the new GL context when the surface gets recreated. Using this class directly probably will work, but you should reimplement your own version so you have more control over how these basic things happen.


Once that renderer is in place, you should be able to add things to the scene using `OSceneManager`, such as with the function `OSceneManager::createMeshSceneNode(String filename)`. IIRC, only obj files are supported **with triangulated faces**. If you try to load something with quads or more complex geometry, it won't render properly. Also, materials are ignored and I don't remember whether or not normals are loaded. None of my 3D stuff used lighting, so that whole side is underdeveloped. Although, if you're looking to do lighting or any modern effects whatsoever, an engine built on OpenGL ES 1.x is probably not a good choice to begin with.


Once you add a node to the scene, everything should work. Just look at the various APIs for the nodes your using to manipulate the scene. The 2D APIs are very similar, but instead of using OSceneManager you use `OOrthoEnvironment`, which has a lot more features/different node types.
