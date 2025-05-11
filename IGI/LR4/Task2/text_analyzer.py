import re
import zipfile
from typing import List, Tuple, Dict
from dataclasses import dataclass
from pathlib import Path

@dataclass
class TextAnalysis:
    """Class for storing text analysis results."""
    total_sentences: int
    narrative_sentences: int
    interrogative_sentences: int
    imperative_sentences: int
    avg_sentence_length: float
    avg_word_length: float
    smiley_count: int
    lowercase_digit_words: List[str]
    lowercase_letters_count: int
    first_v_word: Tuple[str, int]
    filtered_text: str

class TextAnalyzer:
    """Class for analyzing text with various methods."""
    
    @staticmethod
    def read_file(filename: str) -> str:
        """
        Read text from file.
        
        Args:
            filename (str): Path to the input file
            
        Returns:
            str: Text content
        """
        with open(filename, 'r', encoding='utf-8') as file:
            return file.read()
    
    @staticmethod
    def write_file(filename: str, content: str) -> None:
        """
        Write content to file.
        
        Args:
            filename (str): Path to the output file
            content (str): Content to write
        """
        with open(filename, 'w', encoding='utf-8') as file:
            file.write(content)
    
    @staticmethod
    def create_zip(output_file: str, files_to_zip: List[str]) -> None:
        """
        Create a zip archive with the given files.
        
        Args:
            output_file (str): Path to the output zip file
            files_to_zip (List[str]): List of files to include in the zip
        """
        with zipfile.ZipFile(output_file, 'w') as zipf:
            for file in files_to_zip:
                zipf.write(file, Path(file).name)
    
    @staticmethod
    def get_zip_info(zip_file: str) -> Dict[str, Dict]:
        """
        Get information about files in a zip archive.
        
        Args:
            zip_file (str): Path to the zip file
            
        Returns:
            Dict[str, Dict]: Dictionary with file information
        """
        info = {}
        with zipfile.ZipFile(zip_file, 'r') as zipf:
            for file in zipf.filelist:
                info[file.filename] = {
                    'size': file.file_size,
                    'compressed_size': file.compress_size,
                    'date_time': file.date_time
                }
        return info
    
    @staticmethod
    def analyze_text(text: str) -> TextAnalysis:
        """
        Analyze text and return analysis results.
        
        Args:
            text (str): Text to analyze
            
        Returns:
            TextAnalysis: Analysis results
        """
        # Split text into sentences using regex that preserves sentence endings
        sentences = re.split(r'([.!?]+)', text)
        # Combine sentences with their endings
        sentences = [''.join(sentences[i:i+2]).strip() for i in range(0, len(sentences), 2) if sentences[i].strip()]
        
        # Count sentence types
        narrative = len([s for s in sentences if s.endswith('.')])
        interrogative = len([s for s in sentences if s.endswith('?')])
        imperative = len([s for s in sentences if s.endswith('!')])
        
        # Calculate average sentence length (in characters)
        avg_sentence_length = sum(len(s) for s in sentences) / len(sentences) if sentences else 0
        
        # Calculate average word length
        words = re.findall(r'\b\w+\b', text)
        avg_word_length = sum(len(word) for word in words) / len(words) if words else 0
        
        # Count smileys
        smiley_pattern = r'[:;]-*[(\[)\]]+'
        smiley_count = len(re.findall(smiley_pattern, text))
        
        # Find words with lowercase letters and digits
        lowercase_digit_pattern = r"\w*(?:[a-z][0-9]|[0-9][a-z])\w*"
        lowercase_digit_words = re.findall(lowercase_digit_pattern, text)
        
        # Count lowercase letters
        lowercase_letters_count = len(re.findall(r'[a-z]', text))
        
        # Find first word with 'v' and its position
        words_with_v = re.findall(r"\b[vV]\w*", text)
        first_v_word = words_with_v[0]
        
        # Filter text (remove words starting with 's')
        fitered = re.sub(r"\b[sS]\w*", '', text)
        filtered_text = re.sub(r"\s+", " ", fitered).strip()
        
        return TextAnalysis(
            total_sentences=len(sentences),
            narrative_sentences=narrative,
            interrogative_sentences=interrogative,
            imperative_sentences=imperative,
            avg_sentence_length=avg_sentence_length,
            avg_word_length=avg_word_length,
            smiley_count=smiley_count,
            lowercase_digit_words=lowercase_digit_words,
            lowercase_letters_count=lowercase_letters_count,
            first_v_word=first_v_word,
            filtered_text=filtered_text
        )
    
    @staticmethod
    def is_valid_ip(ip: str) -> bool:
        """
        Check if a string is a valid IP address.
        
        Args:
            ip (str): String to check
            
        Returns:
            bool: True if valid IP, False otherwise
        """
        pattern = r'^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$'
        return bool(re.match(pattern, ip))
    
    @staticmethod
    def format_analysis_results(analysis: TextAnalysis) -> str:
        """
        Format analysis results as a string.
        
        Args:
            analysis (TextAnalysis): Analysis results
            
        Returns:
            str: Formatted results
        """
        return f"""Text Analysis Results:
            1. Sentence Statistics:
            - Total sentences: {analysis.total_sentences}
            - Narrative sentences: {analysis.narrative_sentences}
            - Interrogative sentences: {analysis.interrogative_sentences}
            - Imperative sentences: {analysis.imperative_sentences}
            - Average sentence length: {analysis.avg_sentence_length:.2f} characters
            - Average word length: {analysis.avg_word_length:.2f} characters

            2. Special Features:
            - Smiley count: {analysis.smiley_count}
            - Lowercase letters count: {analysis.lowercase_letters_count}
            - First word with 'v': '{analysis.first_v_word[0]}' (position {analysis.first_v_word[1]})

            3. Words with lowercase letters and digits:
            {', '.join(analysis.lowercase_digit_words) or 'None found'}

            4. Text without words starting with 's':
            {analysis.filtered_text}
        """ 