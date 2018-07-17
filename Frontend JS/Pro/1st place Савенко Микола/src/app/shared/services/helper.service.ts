/**
 * @abstract
 * Helper Service contains additional functionality
 */
export abstract class HelperService {

  /**
   * Generate random number
   * @returns {number}
   */
  static getRandomNumber(): string {
    return window.crypto.getRandomValues(new Uint32Array(1))[0] + '';
  }

  /**
   * Load file via FileReader and parse it.
   */
  static readAndParseJSONFile(file: File): Promise<any> {

    return new Promise((resolve, reject) => {

      const reader = new FileReader();

      reader.onload = (e: any) => {
        let parsedfile;

        try {
          parsedfile = JSON.parse(e.target.result);
        } catch (e) {
          throw(e);
        }

        resolve(parsedfile);
      };

      reader.onerror = (err) => reject(err);

      reader.readAsText(file);
    });

  }

  /**
   * Download any data as JSON file format
   * @param data
   * @param {string} fileName
   */
  static downloadAsJSONFile(data: any, fileName: string = 'data') {
    const a = document.createElement('a');
    const file = new Blob([JSON.stringify(data)], {type: 'application/json'});

    a.href = URL.createObjectURL(file);
    a.download = `${fileName}.json`;
    a.click();
  }
}
