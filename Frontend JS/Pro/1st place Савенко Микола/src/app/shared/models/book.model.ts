import {HelperService} from '../services/helper.service';

export interface IBook {
  id: string;
  name: string;
  author: string;
  publicationDate: Date;
  isbn: string;
  coverImageUrl?: string;

  toJSON();
}

/**
 * The Book model is the original book structure
 */
export class Book implements IBook {
  id;
  name;
  author;
  publicationDate;
  isbn;
  coverImageUrl;

  /**
   * Convert json to IBook especially publication date
   * @param json
   * @returns {IBook}
   */
  static fromJSON(json: any): IBook {
    const book = Object.create(Book.prototype);

    return Object.assign(book, json, {
      publicationDate: new Date(json.publicationDate)
    });
  }

  constructor(data: any = {}) {
    this.id = HelperService.getRandomNumber();

    this.name = data.name;
    this.author = data.author;
    this.publicationDate = data.publicationDate;
    this.isbn = data.isbn;
    this.coverImageUrl = data.coverImageUrl;
  }

  /**
   * Method fires on JSON.stringify call
   * @override
   * @returns {ObjectConstructor & {books: any[]}}
   */
  toJSON() {
    return Object.assign(this, {
      publicationDate: this.publicationDate.toString()
    });
  }
}
