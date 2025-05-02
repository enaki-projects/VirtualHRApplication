
const toTitleCase = (str: string) => str.toLowerCase().split(' ').map(function(word) {
    return word.replace(word[0], word[0].toUpperCase());
}).join(' ');

const getShortDescription = (str: string, numberOfChars: number = 10) => {
    if (!str)
        return '-'
    if (str.length < numberOfChars)
        return str
    return str.substring(0, numberOfChars) + '...'
}

const getRandomColor = () => {
    return Math.floor(Math.random()*16777215).toString(16);
}

const extractFieldFromListOfDict = (myList, fieldName) => {
    return myList.map(item => item[fieldName]);
}

export const myUtils = {
    toTitleCase,
    getShortDescription,
    getRandomColor,
    extractFieldFromListOfDict
}

