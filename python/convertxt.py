import os, shutil, re


def converttotxt():
    
    destfolder = "PycharmProjects/TVSeries/txt"
    oldfolder = "PycharmProjects/TVSeries/extracted"
    if not os.path.exists(destfolder):
        os.mkdir(destfolder)
    for folder in os.listdir(oldfolder):
        #turn the .srt file into a .txt file, copy it and move it to a new place
        if folder[0] == ".":print folder,", nope";continue
        for filename in os.listdir(os.path.join(oldfolder, folder)):
            if filename[-3:] == "srt":
                src = os.path.join(oldfolder, folder, filename)
                print "converting %s to %s.txt" % (filename, folder)
                shutil.copyfile(src, os.path.join(destfolder, "%s.txt" % (filename)))
                break
    return


def main():
    converttotxt()


main()
