//Load openDSU enviroment
require("../opendsu-sdk/builds/output/openDSU");
const openDSU = require("opendsu");
const readlineSync = require("readline-sync");
const fs = require('fs/promises');

$$.LEGACY_BEHAVIOUR_ENABLED = true;

// Load OpenDSU services
const keySSISpace = openDSU.loadApi("keyssi");
const resolver = openDSU.loadApi("resolver");

// Function to create a new DSU
const createDSU = () => {
  const templateSSI = keySSISpace.createTemplateSeedSSI("default");
  resolver.createDSU(templateSSI, (err, dsuInstance) => {
    if (err) {
      return console.error("Failed to create DSU:", err);
    }
    dsuInstance.getKeySSIAsString((err, keySSI) => {
      if (err) {
        return console.error("Failed to get DSU KeySSI:", err);
      }
      console.log("New DSU created with KeySSI:", keySSI);
    });
  });
};

// Function to add a file to a DSU using fs/promises
const addFileToDSU = async (keySSIString, filePath) => {
  const keySSI = keySSISpace.parse(keySSIString);
  try {
    const dsu = await new Promise((resolve, reject) => {
      resolver.loadDSU(keySSI, (err, dsuInstance) => {
        if (err) reject(err);
        else resolve(dsuInstance);
      });
    });

    const fileName = filePath.split('/').pop();
    const fileContent = await fs.readFile(filePath, 'utf-8');
    await new Promise((resolve, reject) => {
      dsu.writeFile(fileName, fileContent, (err) => {
        if (err) reject(err);
        else resolve();
      });
    });

    console.log(`File ${fileName} added to DSU.`);
  } catch (err) {
    console.error("Failed to add file to DSU:", err);
  }
};

// Function to explore the content of a DSU
const exploreDSU = (keySSIString) => {
  const keySSI = keySSISpace.parse(keySSIString);
  resolver.loadDSU(keySSI, (err, dsu) => {
    if (err) {
      return console.error("Failed to load DSU:", err);
    }
    dsu.listFiles('/', (err, files) => {
      if (err) {
        return console.error("Failed to list files:", err);
      }
      console.log("Files in DSU:");
      files.forEach(file => console.log(file));

      const fileName = readlineSync.question("Enter the file name to view its content (or press Enter to skip): ");
      if (fileName) {
        dsu.readFile(fileName, (err, content) => {
          if (err) {
            return console.error("Failed to read file:", err);
          }
          console.log(`Content of ${fileName}:\n${content.toString()}`);
        });
      }
    });
  });
};

// Function to rename a file in a DSU
const renameFileInDSU = (keySSIString, oldFileName, newFileName) => {
  const keySSI = keySSISpace.parse(keySSIString);
  resolver.loadDSU(keySSI, (err, dsu) => {
    if (err) {
      return console.error("Failed to load DSU:", err);
    }
    dsu.rename(oldFileName, newFileName, (err) => {
      if (err) {
        return console.error("Failed to rename file:", err);
      }
      console.log(`File renamed from ${oldFileName} to ${newFileName}`);
    });
  });
};

// Function to create a file with content from keyboard input
const createFileWithContentInDSU = (keySSIString, fileName, content) => {
  const keySSI = keySSISpace.parse(keySSIString);
  resolver.loadDSU(keySSI, (err, dsu) => {
    if (err) {
      return console.error("Failed to load DSU:", err);
    }
    dsu.writeFile(fileName, content, (err) => {
      if (err) {
        return console.error("Failed to create file:", err);
      }
      console.log(`File ${fileName} created with provided content.`);
    });
  });
};

// Function to create a directory in a DSU
const createDirectoryInDSU = (keySSIString, dirName) => {
  const keySSI = keySSISpace.parse(keySSIString);
  resolver.loadDSU(keySSI, (err, dsu) => {
    if (err) {
      return console.error("Failed to load DSU:", err);
    }
    dsu.createFolder(dirName, (err) => {
      if (err) {
        return console.error("Failed to create directory:", err);
      }
      console.log(`Directory ${dirName} created in DSU.`);
    });
  });
};

// Function to delete a file in a DSU
const deleteFileInDSU = (keySSIString, fileName) => {
  const keySSI = keySSISpace.parse(keySSIString);
  resolver.loadDSU(keySSI, (err, dsu) => {
    if (err) {
      return console.error("Failed to load DSU:", err);
    }
    dsu.delete(fileName, (err) => {
      if (err) {
        return console.error("Failed to delete file:", err);
      }
      console.log(`File ${fileName} deleted from DSU.`);
    });
  });
};

// Main function to handle commands
const main = () => {
  let command = readlineSync.question("Enter command (createDSU, exploreDSU, addFile, renameFile, createFile, deleteFile, createDir, q): ");
    switch (command) {
      case "createDSU":
        createDSU();
        console.log
        break;
      case "exploreDSU":
        const exploreKeySSI = readlineSync.question("Enter the KeySSI of the DSU to explore: ");
        exploreDSU(exploreKeySSI);
        break;
      case "addFile":
        const addFileKeySSI = readlineSync.question("Enter the KeySSI of the DSU: ");
        const filePath = readlineSync.question("Enter the file path: ");
        addFileToDSU(addFileKeySSI, filePath);
        break;
      case "renameFile":
        const renameFileKeySSI = readlineSync.question("Enter the KeySSI of the DSU: ");
        const oldFileName = readlineSync.question("Enter the current file name: ");
        const newFileName = readlineSync.question("Enter the new file name: ");
        renameFileInDSU(renameFileKeySSI, oldFileName, newFileName);
        break;
      case "createFile":
        const createFileKeySSI = readlineSync.question("Enter the KeySSI of the DSU: ");
        const fileName = readlineSync.question("Enter the new file name: ");
        const content = readlineSync.question("Enter the content for the file: ");
        createFileWithContentInDSU(createFileKeySSI, fileName, content);
        break;
      case "deleteFile":
        const deleteFileKeySSI = readlineSync.question("Enter the KeySSI of the DSU: ");
        const fileNameToDelete = readlineSync.question("Enter the file name: ");
        deleteFileInDSU(deleteFileKeySSI, fileNameToDelete);
        break;
      case "createDir":
        const createDirKeySSI = readlineSync.question("Enter the KeySSI of the DSU: ");
        const dirName = readlineSync.question("Enter the directory name: ");
        createDirectoryInDSU(createDirKeySSI, dirName);
        break;
      default:
        console.log("Unknown command. Please use createDSU, exploreDSU, addFile, or renameFile.");
    }
};

main();