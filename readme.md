Group  42
Vineet Patel (vpate43), Uzair Mohammed (umoham4), Affan Farid (afarid4)

Tested out on systems1 using following commands:
make 
java GameTester MysticCity_51.gdf

GDF file specification for 5.1 is included, along with a sample file. This version is a modified version of 5.0 
UML diagram also included

Major Features Added:
-Added an attack command: "attack" <character_name>
-Added different types of places (Dark, Safe, Healing, Radioactive, Enhanced)
-Added different type of artifacts (Food, Potion, Weapon, Flashlight)
-Added different types of AI (Aggressive, Hoarder, Passive, JohnBell)

What Each Person worked on:
-Vineet Patel 
	-Implemented Place and its subclasses.
	-Modified Game class to a singleton 
	-Implemented the GDF file reading for version 5.2
	-Implemented attack command along with AttackMove
	-Added character health and die function
	
-Uzair Mohammed
	-Implemented AI and its subclasses
	-Added a John Bell class
	
-Affan Farid
	-Implemented Artifact and its subclasses
	-Made UML diagram
	
NOTE: The points mentioned about were the primary focus for each person. Note that each 
group member helped other people (i.e Affan helped Vineet on Enhanced Place, Vineet helped 
Uzair with the aggressiveAI). 
