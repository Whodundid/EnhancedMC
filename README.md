](https://confluence.atlassian.com/x/iqyBMg), or Because there are a great number of things piling up that need work done on the code 
and I am having trouble remembering all of them, I am making this list to document
all of the bugs, glitches, features, oddities, ect. that need to be worked on at
some point in the future.

This list is not in meant to designate priority to one task over another, simply
state what needs to get done.

-------------------------------------------------------------------------------------

    1. Parkour Helper (Storm AI)
        a. Get ordinal edge jump directions working (partially working)
        
        b. Create an algorithm that can determine appropriate jump vector based on
           any current look vector approaching a helper block. The way helper blocks
           currently store the jump direction information would have to change.
           
        c. Create a proper system for determining the type of jump to be performed.
        
        d. Fix half-block helper block registration in auto detection.
        
        e. Create neo jump scripts.
        
Fixed---f. There is a bug where a null pointer is thrown if the world is unloaded
           while the background checker is running.
           -Added a kill switch that fires during a world unload event.
           
        g. Bug where graphics are drawn over jump success text.
        
        
        
        
        
    2. PlayerFacing API
        a. Get graduallyFaceDir() working.
        
        b. If possible, extend isFacingPlayer (entityHit) range significantly
           further out. (currently limited to player's own reach)
           
           
           
           
    
    3. SkinLayerSwitcher
        a. Fix bug causing flipped parts to load in an improper orientation.
        
        b. Fix bug causing parts to load as all off.
        
Fixed?--c. If possible, draw capes in both the MainGUI and the PartGUI.
        
        
        
        
        
    4. PlayerNameHistory and Skin Viewer
        a. Fix concurrency issue when fetching both name history and player skin.
        
        b. Fix http link error when deconstructing the session information.
        
        c. Fully integrate player skin fetcher with name history fetcher.
        
        
        
        
        
    5. GlobalSettings
Fixed---a. There are still some instances where certain sub mod methods can be
           fired even when that specific mod is disabled.
        
Done----b. Sub mod overlap needs to be more properly addressed especially concerning
           hotkeys and scripts, as a global script kill switch requires hotkeys to
           be currently activated. Either there will have be specific instances
           where code from one mod requiring the other can still run, or both will
           have to be either be fully off or on.
           
Fixed---c. The gui is a meme and should probably be a bit less of one.
        
Fixed---d. The settings are not remembered when the game is closed.
        
Fixed---e. There is no button for toggling autocorrect. (button made, not working)

Done----f. Add a smaller button off to the side that opens up that specific
           sub-mods options/config menu. (might even swap the button functions)
        
        
        
        
        
    6. WorldEditListener
Fixed---a. Bug when converting world edit positions into Vector3D coordinates.
        
        
        
        
        
    7. Modded In-Game Chat System
Done----a. Find a way to get this working.
        
        
        
        
        
    8. Overall Code Organization
Yeet----a. Good progress has been made cleaning out the EventListener and ChatDrawer
           classes and moving sub mod specifc code to their own respectable classes,
           but there is still more work to be done.
           
Fixed---b. Class overlap with Vector3D (mine) and Vec3 (minecraft). Vector3D should
           be able to accept and convert Vec3 into a working form that can deal
           with both ints and doubles.
           
           
           
           
           
           
    10. MiniMap
Done----a. Minimap works, a border would be nice though.
        
Fixed---b. When map is drawn, hunger bar is broken.
        
Fixed---c. It appears that there is a massive memory leak.
           -Added InGameTextureHandler which properly takes care
            of DynamicTexture updates.
        
Done----d. Added cardinal directions which rotate with the minimap when drawn.

Fixed---e. No idea where or how this bug popped up from, but the map is only drawn if 
           the player is holding anything in the hotbar or if chat is actively being
           drawn on the screen. (same applies to world editor)


        
        
        
    11. Hotkeys
Done----a. Add a //move hotkey to world edit hotkeys
        
        b. Add toggleable input delay to all hotkeys to prevent accidental key fires.
        
Done----c. Rewrite hotkey system to accommodate modular keys and hotkey creation in game
        
Done----d. Create hotkey gui
        
Fixed--e. Create saving/loading system to file system
        
        f. not sure if it is a bug or even worth a fix, but hotkey combinations 
           ie (ctrl + shift + e) can be activated by pressing the buttons in any
           given order. It's weird, but it's not broken by any means.
           (Current setup continually detects if the keys are pressed and will
            toggle states if buttons are pressed beyond the input delay period)
           -Added a small amount of input delay to prevent accidental presses changing
            the current sprint setting.
            
        
        
        
        
    12. World Editor
Partial-a. Get proof of concept working.

        b. Finish main interface
            1. Get all tools working
Partial---------a. Select (zooming does not properly grab coordinates, many issues with visual selection box)
                b. Move Select
                c. Move Selection
Done------------d. Pan (need to get working for 3D view) (also done)
                e. Pencil
                f. Paint Bucket
                g. Brush
	    2. Fix pixel/world coordinate alignment (broken when scrolling)
            3. Add block palette
Partial-----4. Add vertical view
                -multiple problems
Partial-----5. Add 3D view (looking for more efficient rendering approach)
            6. Get proper graphics for all editor contents
        
        c. Add file select menu
        
        d. Add starter menu to create/load project files
        
        e. Add editor settings GUI
        
        f. Add rotate by 90 degrees buttons
        
Fixed---g. Fix coordinate text fields not being able to be focused except only when the mouse is clicked
        
Fixed---h. Implement time delay between closing and reopening the editor
            -Some resources do not properly reload upon quickly closing and re-opening
        
Fixed---i. bug where resources for right click menu are not drawn
            
            
Done--------AutoCorrect system:
InProgress------AutoCorrect gui:
                    -bug when deleting commands from list where list doesn't properly update
                    -focused lines aren't automatically set at current text line
                    -add reset to defaults
                        -add confirmation dialogue box
Done------------Save/load AutoCorrect commands
            
            blink gui
            
            autogm3 gui
            
            clearWaterLavaFire gui
            gamma slider
            
            BiomeReplace
            BiomePaint
            BiomeCopy
            BiomePaste
            
            top down mini map
            
            redo parkour ai
            
Done--------finish settings gui pages
            
            finish name history skin veiwer
            
            script gui
            script runner
            scripting language
            
            clear water lava gui
            
------------------------------------------------------------------------------
            
            enhancedgui:
Done------------button:
                    -somehow fix visual streching
                slider
                drop down list:
                    -scrollable
	        textFields:
Fixed----------------sometimes can't backspace
Fixed----------------backspacing is weird
Fixed----------------doesn't accept repeat events

------------------------------------------------------------------------------
                
            hotKeyGui:
                -bug where toggling enabled doesn't visually update the key list.
                -reset to defaults doesn't do anything
            
            ModdedChat:
Fixed------------can't auto complete names
Fixed------------can't auto complete commands
Fixed------------can't click on elements in chat history
Fixed------------new entries for chatTypes aren't automatically added to chat history
Partial----------entries for chatTypes aren't properly fit into the chat history
Fixed------------text isn't entered into input field if the input field looses focus
            
            multiHotbar:
                -visual is not updating correctly
                -scrolling flip flops order while traversing
                -only 2 layers "properly" work
                -create large one-layer hotbar
                
            miniMapGui:
                -make drawn position for each element customizeable and resetable
                -create 3 different selectable drawn size