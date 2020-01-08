#export PATH=$PATH:/home/benjamin/CS/java_project

import webbrowser
from bs4 import BeautifulSoup
import urllib

#***selenium options***
#takes the website converter and downloads the video using selenium
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from selenium.webdriver.firefox.options import Options
options = webdriver.FirefoxProfile();
options.set_preference("browser.download.folderList",2);
options.set_preference("browser.download.manager.showWhenStarting", False);
options.set_preference("browser.download.dir","/home/benjamin/Downloads/music_downloads");
options.set_preference("browser.download.manager.alertOnEXEOpen", False);
options.set_preference("browser.download.manager.closeWhenDone", False);
options.set_preference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream,audio/mpeg");
browser = webdriver.Firefox(options);
browser.get("https://ytmp3.cc/en1/")

#***end of selenium options***

#creates a music download folder
from os import mkdir
mkdir("/home/benjamin/Downloads/music_downloads")

#take search results and put in a list of strings
songList = open("song_list.txt", "r")
searchArray = []
for search in songList:
    searchArray.append(search)



#pull up youtube result of testString
for search in searchArray:
    testArray = search.split()
    searchQuery = "http://www.youtube.com/results?search_query="
    count = 0
    for word in testArray:
        searchQuery = searchQuery + word
        if count < len(testArray) - 1:
            searchQuery = searchQuery + "+"
            count = count + 1



    print(searchQuery)
    #takes the first search result, then creates a critical link to the video
    # https://stackoverflow.com/questions/29069444/returning-the-urls-as-a-list-from-a-youtube-search-query
    firstLinksPackage = urllib.urlopen(searchQuery)
    allLinksList = firstLinksPackage.read()
    allLinks = BeautifulSoup(allLinksList, 'html.parser')
    criticalLink = ""
    for vid in allLinks.findAll(attrs={'class':'yt-uix-tile-link'}):
        criticalLink = 'https://www.youtube.com' + vid['href']
        break

    print(criticalLink)


    linkElem = browser.find_element_by_id("input")
    linkElem.send_keys(criticalLink)
    linkElem.submit()

    #clicks download and downloads to folder
    downloadElem = WebDriverWait(browser, 100).until(
            EC.presence_of_element_located((By.LINK_TEXT, "Download"))
        )
    downloadElem.click()

    #clicks convert next
    nextElem = WebDriverWait(browser, 100).until(
        EC.presence_of_element_located((By.LINK_TEXT, "Convert next"))
        )
    nextElem.click()

browser.quit()
print("finished")
