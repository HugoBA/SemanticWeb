import os

import Server

if __name__ == "__main__" :
	#First Step : Launch the RequestToText programm
	
	#Check presence of file
	if os.path.exists("Texts.xml") == False:
		print("Erreur ! Texts mal generes, voir module 1.")
	
	#Second Step : Launch the TextToURI programm
	os.system("java -jar TextToURI.jar 0.3 0")
	
	#Check presence of file
	if os.path.exists("Texts.xml") == False:
		print("Erreur ! Texts mal generes, voir module 1.")