import random

import requests

# the program extracts text and image data for a given topic
# and writes them in the CURRENT DIRECTORY, relative to the location the py script is invoked from
# make sure you set it properly, in case you dont want the default one

# this is the title we will search
f = open("/Wikipedia_topics", encoding="utf8")
count = 0
t = 0
tl = [x for x in f]
topics = random.sample(tl, 8000)

for t in topics:
    found = 0
    temp = ""
    if t[-1] == '\n':
        temp = t[0:-1]
    for char in temp:
        if char.isalpha() or char == "_":
            """Nothing"""
        else:
            found = 1
            break
    if found == 0:
        topic = temp
        # this is the config for to get the first introduction of a title
        text_config = {
            'action': 'query',
            'format': 'json',
            'titles': topic,
            'prop': 'extracts',
            'exintro': True,
            'explaintext': True,
        }
        try:
            text_response = requests.get('https://en.wikipedia.org/w/api.php', params=text_config).json()
            text_page = next(iter(text_response['query']['pages'].values()))
            if text_page['extract'] == "":
                continue
            else:
                print(text_page['title'])
                file1 = open(text_page['title'] + ".txt", "w", encoding="utf8")  # write mode
                file1.write(text_page['extract'])
                file1.close()
            if count == 1012:
                break
            else:
                count += 1
        except:
            print('Exception')
    # ************************************references*******************************************************************
# https://www.mediawiki.org/wiki/API:Parsing_wikitext
# https://www.mediawiki.org/wiki/Extension:TextExtracts#Caveats
# https://stackoverflow.com/questions/58337581/find-image-by-filename-in-wikimedia-commons
# https://en.wikipedia.org/w/api.php?action=query&titles=File:Albert_Einstein_Head.jpg&prop=imageinfo&iiprop=url

# https://stackoverflow.com/questions/24474288/how-to-obtain-a-list-of-titles-of-all-wikipedia-articles
# for all titles
# https://dumps.wikimedia.org/enwiki/latest/enwiki-latest-all-titles-in-ns0.gz
# https://en.wikipedia.org/w/api.php?action=parse&pageid=252735&prop=images
