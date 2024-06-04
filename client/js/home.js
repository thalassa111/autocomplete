// Function to handle autocomplete TRIE
function autocompleteTrie(inputField) {
    // Function to update the suggestions list with autocomplete suggestions
    function updateSuggestions(suggestions) {
        const suggestionsList = document.getElementById('suggestionsListTrie');

        // Clear previous suggestions
        suggestionsList.innerHTML = '';

        // Populate the list with new suggestions
        suggestions.forEach(suggestion => {
            const listItem = document.createElement('li');
            listItem.textContent = suggestion;
            suggestionsList.appendChild(listItem);
        });

        // Show the suggestions list
        suggestionsList.style.display = 'block';
    }

    // Hide the suggestions list
    function hideSuggestions() {
        const suggestionsList = document.getElementById('suggestionsListTrie');
        suggestionsList.style.display = 'none';
    }

    // Function to fetch autocomplete suggestions from the backend
    function fetchSuggestions(input) {
        const startTime = performance.now();
        return fetch("http://localhost:8080/algoritm/trie?input=" + `${input}`)
            .then(response => response.json())
            .then(data => {
                updateSuggestions(data);
                const endTime = performance.now();
                updateLogTextarea("timeLogTrie", "prefix: " + `${(endTime - startTime).toFixed(4)}\n`);
            })
            .catch(error => {
                console.error('Error fetching suggestions:', error);
            });
    }

    // Event listener for input field
    inputField.addEventListener('input', function() {
        const inputValue = this.value.trim(); // Trim whitespace from input value

        if (inputValue === '') {
            hideSuggestions();
        } else {
            // Fetch suggestions from the backend
            fetchSuggestions(inputValue);
        }
    });

    // Add click event listener to suggestions list
    const suggestionsList = document.getElementById('suggestionsListTrie');
    suggestionsList.addEventListener('click', function(event) {
        // Check if the clicked element is an <li>
        if (event.target.tagName === 'LI') {
            // Set the value of the input field to the text content of the clicked list item
            inputField.value = event.target.textContent;
            // Hide the suggestions list
            hideSuggestions();
        }
    });
}

// Function to handle autocomplete HASH
function autocompleteHashTable(inputField) {
    // Function to update the suggestions list with autocomplete suggestions
    function updateSuggestions(suggestions) {
        const suggestionsList = document.getElementById('suggestionsListHash');

        // Clear previous suggestions
        suggestionsList.innerHTML = '';

        // Populate the list with new suggestions
        suggestions.forEach(suggestion => {
            const listItem = document.createElement('li');
            listItem.textContent = suggestion;
            suggestionsList.appendChild(listItem);
        });

        // Show the suggestions list
        suggestionsList.style.display = 'block';
    }

    // Hide the suggestions list
    function hideSuggestions() {
        const suggestionsList = document.getElementById('suggestionsListHash');
        suggestionsList.style.display = 'none';
    }

    // Function to fetch autocomplete suggestions from the backend (hash table)
    function fetchSuggestions(input) {
        const startTime = performance.now();
        return fetch("http://localhost:8080/algoritm/hash?input=" + `${input}`)
            .then(response => response.json())
            .then(data => {
                updateSuggestions(data);
                const endTime = performance.now();
                updateLogTextarea("timeLogHash", "prefix: " + `${(endTime - startTime).toFixed(4)}\n`);
            })
            .catch(error => {
                console.error('Error fetching suggestions:', error);
            });
    }

    // Event listener for input field
    inputField.addEventListener('input', function() {
        const inputValue = this.value.trim(); // Trim whitespace from input value

        if (inputValue === '') {
            hideSuggestions();
        } else {
            // Fetch suggestions from the backend
            fetchSuggestions(inputValue);
        }
    });

    // Add click event listener to suggestions list
    const suggestionsList = document.getElementById('suggestionsListHash');
    suggestionsList.addEventListener('click', function(event) {
        if (event.target.tagName === 'LI') {
            inputField.value = event.target.textContent;
            hideSuggestions();
        }
    });
}

// Function to handle autocomplete using ngrams
function autocompleteNgrams(inputField) {
    // Function to update the suggestions list with autocomplete suggestions
    function updateSuggestions(suggestions) {
        const suggestionsList = document.getElementById('suggestionsListNgrams');

        // Clear previous suggestions
        suggestionsList.innerHTML = '';

        // Populate the list with new suggestions
        suggestions.forEach(suggestion => {
            const listItem = document.createElement('li');
            listItem.textContent = suggestion;
            suggestionsList.appendChild(listItem);
        });

        // Show the suggestions list
        suggestionsList.style.display = 'block';
    }

    // Hide the suggestions list
    function hideSuggestions() {
        const suggestionsList = document.getElementById('suggestionsListNgrams');
        suggestionsList.style.display = 'none';
    }

    // Function to fetch autocomplete suggestions from the backend (ngrams)
    function fetchSuggestions(input) {
        const startTime = performance.now();
        return fetch("http://localhost:8080/algoritm/ngram?input=" + `${input}`)
            .then(response => response.json())
            .then(data => {
                updateSuggestions(data);
                const endTime = performance.now();
                updateLogTextarea("timeLogNgram", "prefix: " + `${(endTime - startTime).toFixed(4)}\n`);
            })
            .catch(error => {
                console.error('Error fetching suggestions:', error);
            });
    }

    // Event listener for input field
    inputField.addEventListener('input', function() {
        const inputValue = this.value.trim(); // Trim whitespace from input value

        if (inputValue === '') {
            hideSuggestions();
        } else {
            // Fetch suggestions from the backend
            fetchSuggestions(inputValue);
        }
    });

    // Add click event listener to suggestions list
    const suggestionsList = document.getElementById('suggestionsListNgrams');
    suggestionsList.addEventListener('click', function(event) {
        if (event.target.tagName === 'LI') {
            inputField.value = event.target.textContent;
            hideSuggestions();
        }
    });
}

// Function to handle saving trie
function loadDBTrie() {
    const startTime = performance.now();
    fetch("http://localhost:8080/algoritm/loadDBTrie", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to save trie');
        }
        const endTime = performance.now();
        updateLogTextarea("timeLogTrie", "load: " +  `${(endTime - startTime).toFixed(4)}\n`)
        console.log('Trie saved successfully');
    })
    .catch(error => {
        console.error('Error saving trie:', error);
    });
}

function insertWordTrie() {
    const word = document.getElementById('myInputTrie').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/insertWordTrie?word=' + word, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
            const endTime = performance.now();
            updateLogTextarea("timeLogTrie", "insert: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word inserted successfully!');
            } else {
                alert('Failed to insert word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

function deleteWordTrie() {
    const word = document.getElementById('myInputTrie').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/deleteWordTrie?word=' + word, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                const endTime = performance.now();
                updateLogTextarea("timeLogTrie", "delete: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word deleted successfully!');
            } else {
                alert('Failed to delete word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

function loadDBHash() {
    const startTime = performance.now();
    fetch("http://localhost:8080/algoritm/loadDBHash", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to save hash');
        }
        const endTime = performance.now();
        updateLogTextarea("timeLogHash", "load: " +  `${(endTime - startTime).toFixed(4)}\n`)
        console.log('Hash saved successfully');
    })
    .catch(error => {
        console.error('Error saving hash:', error);
    });
}

function insertWordHash() {
    const word = document.getElementById('myInputHash').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/insertWordHash?word=' + word, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
            const endTime = performance.now();
            updateLogTextarea("timeLogHash", "insert: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word inserted successfully!');
            } else {
                alert('Failed to insert word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

function deleteWordHash() {
    const word = document.getElementById('myInputHash').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/deleteWordHash?word=' + word, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                const endTime = performance.now();
                updateLogTextarea("timeLogHash", "delete: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word deleted successfully!');
            } else {
                alert('Failed to delete word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

function loadDBNgram() {
    const startTime = performance.now();
    fetch("http://localhost:8080/algoritm/loadDBNgram", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to save Ngram');
        }
        const endTime = performance.now();
        updateLogTextarea("timeLogNgram", "load: " +  `${(endTime - startTime).toFixed(4)}\n`)
        console.log('Ngram saved successfully');
    })
    .catch(error => {
        console.error('Error saving Ngram:', error);
    });
}

function insertWordNgram() {
    const word = document.getElementById('myInputNgrams').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/insertWordNgram?word=' + word, {
            method: 'POST'
        }).then(response => {
            if (response.ok) {
            const endTime = performance.now();
            updateLogTextarea("timeLogNgram", "insert: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word inserted successfully!');
            } else {
                alert('Failed to insert word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

function deleteWordNgram() {
    const word = document.getElementById('myInputNgrams').value;
    if (word) {
        const startTime = performance.now();
        fetch('http://localhost:8080/algoritm/deleteWordNgram?word=' + word, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                const endTime = performance.now();
                updateLogTextarea("timeLogNgram", "delete: " +  `${(endTime - startTime).toFixed(4)}\n`)
                alert('Word deleted successfully!');
            } else {
                alert('Failed to delete word.');
            }
        });
    } else {
        alert('Please enter a word.');
    }
}

// Function to update the content of the specified textarea
function updateLogTextarea(textareaId, message) {
    const textarea = document.getElementById(textareaId);
    if (textarea) {
        textarea.value += message; // Append message to the existing content
        textarea.scrollTop = textarea.scrollHeight; // Auto-scroll to the bottom
    }
}

// Call the autocomplete function passing the input field
const inputField = document.getElementById('myInputTrie');
autocompleteTrie(inputField);

// Call the autocomplete function passing the input field
const inputFieldHashTable = document.getElementById('myInputHash');
autocompleteHashTable(inputFieldHashTable);

// Call the autocomplete function passing the input field
const inputFieldNgrams = document.getElementById('myInputNgrams');
autocompleteNgrams(inputFieldNgrams);

// Add event listener to save trie button
const saveTrieButton = document.querySelector('#suggestionsListTrie + .container button');
if (saveTrieButton) {
    saveTrieButton.addEventListener('click', loadDBTrie);
}
const saveHashButton = document.querySelector('#suggestionsListHash + .container button');
if (saveHashButton) {
    saveHashButton.addEventListener('click', loadDBHash);
}
const saveNgramButton = document.querySelector('#suggestionsListNgrams + .container button');
if (saveNgramButton) {
    saveNgramButton.addEventListener('click', loadDBNgram);
}