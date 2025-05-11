from .text_analyzer import TextAnalyzer

def run_task2():
    """Run Task 2: Text Analysis."""
    analyzer = TextAnalyzer()
    
    # Get input file path
    input_file = input("Enter path to input text file: ")
    input_file = f"Task2/{input_file}"
    
    try:
        # Read and analyze text
        text = analyzer.read_file(input_file)
        analysis = analyzer.analyze_text(text)
        
        # Format and save results
        results = analyzer.format_analysis_results(analysis)
        output_file = "Task2/text_analysis_results.txt"
        analyzer.write_file(output_file, results)
        
        # Create zip archive
        zip_file = "Task2/text_analysis.zip"
        analyzer.create_zip(zip_file, [output_file])
        
        # Get and display zip info
        zip_info = analyzer.get_zip_info(zip_file)
        print("\nZip Archive Information:")
        for filename, info in zip_info.items():
            print(f"\nFile: {filename}")
            print(f"Size: {info['size']} bytes")
            print(f"Compressed size: {info['compressed_size']} bytes")
            print(f"Date/Time: {info['date_time']}")
        
        # Test IP validation
        print("\nIP Address Validation:")
        test_ips = ["127.0.0.1", "255.255.255.0", "1300.6.7.8", "abc.def.gha.bcd"]
        for ip in test_ips:
            is_valid = analyzer.is_valid_ip(ip)
            print(f"{ip}: {'Valid' if is_valid else 'Invalid'}")
        
        print("\nAnalysis complete! Results saved to text_analysis_results.txt and text_analysis.zip")
        
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    run_task2() 