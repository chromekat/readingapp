export interface Book {
    bookId: number;
    title: string;
    author: string;
    yearPublished?: string;
    pageCount: number;
    pagesRead: number;
    status: BookStatus;
    rating?: number;
}

export enum BookStatus{
    CURRENTLY_READING = 'CURRENTLY_READING',
    HAVE_READ = 'HAVE_READ',
    WANT_TO_READ = 'WANT_TO_READ',
    DID_NOT_FINISH = 'DID_NOT_FINISH'
}