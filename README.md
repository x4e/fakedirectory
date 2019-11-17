# Fake Directory [![Build Status](https://travis-ci.com/cookiedragon234/fakedirectory.svg?branch=master)](https://travis-ci.com/cookiedragon234/fakedirectory)
Exploits a flaw in pretty much every zip file parser, which treats files ending with a `/` as a directory, even if they aren't.

This makes the contents of the files completely unavailable to anyone using WinRAR, Luyten and every other zip viewer I have seen.

## Why does it work?
Look at this code from `java/util/zip/ZipFile`:
```Java
    /**
     * Returns the zip file entry for the specified name, or null
     * if not found.
     *
     * @param name the name of the entry
     * @return the zip file entry, or null if not found
     * @throws IllegalStateException if the zip file has been closed
     */
    public ZipEntry getEntry(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        long jzentry = 0;
        synchronized (this) {
            ensureOpen();
            jzentry = getEntry(jzfile, zc.getBytes(name), true);
            if (jzentry != 0) {
                // If no entry is found for the specified 'name' and
                // the 'name' does not end with a forward slash '/',
                // the implementation tries to find the entry with a
                // slash '/' appended to the end of the 'name', before
                // returning null. When such entry is found, the name
                // that actually is found (with a slash '/' attached)
                // is used
                // (disabled if jdk.util.zip.ensureTrailingSlash=false)
                ZipEntry ze = ensuretrailingslash ? getZipEntry(null, jzentry)
                                                  : getZipEntry(name, jzentry);
                freeEntry(jzfile, jzentry);
                return ze;
            }
        }
        return null;
    }
```
When the JVM comes across a reference to a class, say for example the Main Class specified in the manifest, it searches the ZipFile for that class. If a file with that specific name does not exist it tries again with a `/` appended to the name. Therefore it will still find the given entry, even though readers will skip over the file presuming its a directory.

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
