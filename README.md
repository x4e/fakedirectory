# Fake Directory [![Build Status](https://travis-ci.com/cookiedragon234/fakedirectory.svg?branch=master)](https://travis-ci.com/cookiedragon234/fakedirectory)
Exploits a flaw in pretty much every zip file parser, which treats files ending with a `/` as a directory, even if they aren't.

This makes the contents of the files completely unavailable to anyone using WinRAR, Luyten and every other zip viewer I have seen.

## How can this be leveraged
One usage of this is as a form of Java Jar obfuscation. The JVM is not vulnerable to this exploit, meaning it will read a file `test.class/` as a file rather than a directory. This means that you can produce valid Jar files that are able to be executed but not analysed or decompiled easily.

This could also have malicious uses, such as to hide malware within a zip file. I have not tested whether anti viruses are vulnerable, however there is a possibility that some might be. This is why this needs to be publicised so that it can be fixed.

Credit to [@Cubxity](https://github.com/Cubxity) and [@half-cambodian-hacker-man](https://github.com/half-cambodian-hacker-man) for providing information on discord.

## Usage
`java -jar fakedirectory.jar exampleJar.jar` will replace all class files within the jar with a fake directory.

## Examples of vulnerable applications
Winrar

![WinRAR Failing](https://i.imgur.com/pKn2FOO.png)

Luyten

![Luyten Failing](https://i.imgur.com/rkkUNEJ.png)

JByteMod

![JByteMod Failing](https://i.imgur.com/awhPq65.png)


Only use this for educational purposes.
