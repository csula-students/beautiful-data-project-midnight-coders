import os, zipfile, json, numpy, re, nltk

def data_output(countries, source, dest):

    print "show\tseason\tepisode\tcountry\tzipsize\tunzipsize\tratio\tlines\twords per line\tWPL std dev\tunique words\tgenre\tyear"
    datadict = {}
    output = ""
    for country in countries:
        countryoutput = ""
        directory = "subtitles/%stxts" % (country)
        showinfo = json.loads((file.readFile("subtitles/%ssubs/%sinfo.txt" % (country, country))))
        for filename in os.listdir("subtitles/%stxts/zipped" % (country)):
            datapoint = {}
            getdata(filename, directory, datapoint, showinfo, source)
            countryoutput += printdatapoint(datapoint)+"\n"
            datadict[filename] = datapoint
        output += countryoutput
        name = "subtitles/%stxts/%sdata%s.txt" % (country, country, source)
        save_dict(datadict, name)
    file.writeFile(dest, output)

def getdata(filename, directory, datapoint, showinfo, source):
    show = filename[0:-4]
    datapoint["show"] = show
    datapoint["episode"] = filename[-2:]
    datapoint["season"] = filename[-4:-3]
    zipsource = os.path.join(directory, source+"zipped", filename)
    unzipsource = os.path.join(directory, source, filename+".txt")
    datapoint["zipsize"] = os.path.getsize(zipsource)
    datapoint["unzipsize"] = os.path.getsize(unzipsource)
    datapoint["ratio"] = float(datapoint["zipsize"])/datapoint["unzipsize"]
    lineinfo = get_lineinfo(directory, filename)
    datapoint["lines"] = lineinfo["lines"]
    datapoint["wpl"] = lineinfo["wpl"]
    datapoint["wpl st dev"] = lineinfo["wpl st dev"]
    datapoint["unique"] = lineinfo["unique"]
    datapoint["country"] = showinfo[show]["Country"]
    datapoint["genre"] = showinfo[show]["Genre"]
    datapoint["year"] = showinfo[show]["Year"][:4]
    return

def printdatapoint(datapoint):
    """
    print and return the information about an episode
    """
    data = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s" % (
         datapoint["show"], datapoint["season"], datapoint["episode"],
         datapoint["country"], datapoint["zipsize"], datapoint["unzipsize"],
         datapoint["ratio"], datapoint["lines"], datapoint["wpl"],
         datapoint["wpl st dev"], datapoint["unique"], datapoint["genre"],
         datapoint["year"])
    print data
    return data

def get_lineinfo(directory, filename):
    lineinfo = {}

    content = file.readFile(os.path.join(directory, "unzipped", filename+".txt"))

    uniquewords = set(nltk.word_tokenize(content))
    lineinfo["unique"] = len(uniquewords)

    lines = re.compile('.+\n?.*\n\n')
    lineslist = lines.findall(content)
    lineinfo["lines"] = len(lineslist)

    linelengths = []
    for line in lineslist:
        linelength = len(nltk.word_tokenize(line))
        linelengths.append(linelength)
    lineinfo["wpl"] = numpy.mean(linelengths)
    lineinfo["wpl st dev"] = numpy.std(linelengths)
    return lineinfo

def save_dict(dictionary, filename):

    contents = json.dumps(dictionary, indent=4, sort_keys=True)
    print "saving info to "+filename
    file.writeFile(filename, contents)
    return