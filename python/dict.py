import numpy, files, os, json, pickle
import nltk

ngrams = []
def makengram(country, n, source):
   
    directory = "PycharmProjects\\TVSeries\\txt"
    global ngrams
    for filename in os.listdir(directory+source):
        #split each file in the country-specific directory into ngrams
        if filename[0] == ".":
            #skip the files that are of no use to us
            print "skipping", filename
            continue
        print "running", filename
#        content = file.readFile(directory+source+"/"+filename)
        content = open(directory+source+"/"+filename, 'r')
        print str(content)
        #first we have to spit each file into individual words
        words = nltk.word_tokenize(content)
        #then we can run the proper nltk function on the list of words to get the ngrams
        if n == 1:
            ngrams += words
        elif n == 2:
            ngrams += nltk.bigrams(words)
        elif n == 3:
            ngrams += nltk.trigrams(words)
        else:
            ngrams += nltk.util.ngrams(words, n)
    #save the list of ngrams in a text file that can easily be read by python later
    print "saving %s%s%s%dgrams.txt" % (directory, source, country, n)
    files.writeFile("%s%s%s%dgrams.txt" % (directory, source, country, n), (pickle.dumps(ngrams)))
    print "saved."
    return


def plaintext(ngrams, n):
 
    topstr = ""
    for item in ngrams:
        if n == 1: topstr += item
        else:
            for string in item:
                topstr += string+" "
        topstr += "\n"
    return topstr


def makedict(country, n, lengths):
   
    print ("loading ngrams from subtitles/%stxts/lowercase%s%dgrams.txt" % (country, country , n))
    ngrams = pickle.loads(files.readFile("subtitles/%stxts/lowercase%s%dgrams.txt" % (country, country, n)))
    #find the frequencies of all the ngrams
    print "determining frequency..."
    fdist = nltk.FreqDist(ngrams)
    for length in lengths:
        #make a plaintext dictionary for each length in lengths
        top = fdist.keys()[:length]
        topstr = plaintext(top, n)
        #save each dictionary
        print ("saving to subtitles/%stxts/dictsizes/%dgram/lowercase%s%dgramdict%d.txt"
               % (country, n, country, n, length))
        files.writeFile("subtitles/%stxts/dictsizes/%dgram/lowercase%s%dgramdict%d.txt"
                        % (country, n, country, n, length), topstr)
    return


def concatenatefiles(country, dictsizes, xgram, source):
   
    for dictsize in dictsizes:
        ngrams = files.readFile("subtitles/%stxts/dictsizes/%dgram/%s%s%dgramdict%d.txt"
                                % (country, xgram, source if source=="lowercase" else "",
                                   country, xgram, dictsize))
        for filename in os.listdir("subtitles/%stxts/%s" % (country, source)):
            print "concatenating %s %dgram %d with %s" % (source, xgram, dictsize, filename)
            content = files.readFile(os.path.join("subtitles/%stxts/%s" % (country, source),filename))
            concatenated = ngrams + "\n\n" + content
            dest = ("subtitles/%stxts/dictsizes/%dgram/%sunzipped/%sdict%d"
                    % (country, xgram, source if source=="lowercase" else "",
                       country, dictsize))
            if not os.path.exists(dest):
                os.mkdir(dest)
            files.writeFile(os.path.join(dest, filename), concatenated)
    return


def main():
    makengram("",10,"")
    plaintext(ngrams,10)
    makedict("",10,10)
    concatenatefiles("",10,"","")


main()