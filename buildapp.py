import subprocess
import shutil
import platform

#mode = input("Choose Deploy Mode:\n1) Debug\n2) Production \n\n-> ")
##subprocess.run("firebase deploy --only hosting", shell=True, check=True)
if platform.system() == "Linux":
    subprocess.run("clear", shell=True, check=True)
else: 
    subprocess.run("cls", shell=True, check=True)


print("===========================")
print("\u001b[32mAndroid BuildPy")
print("\u001b[0mPython Script by Devesh")
print("===========================\n")
print("starting...");

if platform.system() == "Linux":
    subprocess.run("./gradlew clean", shell=True, check=True)
else:
    subprocess.run("gradlew clean", shell=True, check=True)
#subprocess.run("gradlew cleanBuildCache", shell=True, check=True)

print("\nStarted Build :) \n")
#subprocess.run("gradlew assembleRelease", shell=True, check=True)
if platform.system() == "Linux":
    subprocess.run("./gradlew assemble bundle appDistributionUploadRelease appDistributionUploadInternal --stacktrace", shell=True, check=True)
else:
    subprocess.run("gradlew assemble bundle appDistributionUploadRelease appDistributionUploadInternal --stacktrace", shell=True, check=True)






#subprocess.run("gradlew assembleRoboTestRelease --stacktrace", shell=True, check=True)
#subprocess.run("gradlew assembleInternalRelease bundleInternalRelease ", shell=True, check=True)

print("\n=====================\n")
print("\nCopying Generated Android App Files (APK/Bundles)\n")

try: 
    shutil.rmtree("APK")
    #print("old APK Folder Deleted successfully") 
except OSError as error:
    print("Error"+ error)


shutil.copytree('app/build/outputs/apk', "APK/apk")

shutil.copytree('app/build/outputs/bundle', "APK/bundle")


print("\n====================================\n")
print("\u001b[32mSUCCESS: \u001b[0mAndroid APK & App Bundles Generated")
print("\n====================================\n")

#subprocess.run("gradlew bundleRelease", shell=True, check=True)

#subprocess.run("gradlew bundlePlayStore bundleGalaxyStore bundleInternal bundleUniversal", shell=True, check=True)

#print("\n=========================================\n")
#print("App Bundle Generated")
#print("\n=========================================\n")

k=input("press enter to exit") 



