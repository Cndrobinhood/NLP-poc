import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

public class NlpExample {

	public static void main(String[] args) throws Exception {
		String data = readFileAsString("C:\\Users\\Cnd\\Desktop\\test.txt");
		sentDetect(data);
		
		chunk();
		// print(data);
	}

	// reading my file
	public static String readFileAsString(String fileName) throws Exception {
		String data;
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		return data;
	}

	// print data
	public static void print(String data) {
		System.out.println(data);
	}

	public static void sentDetect(String sentence) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("C:/Users/Cnd/Downloads/opennlpModels/en-sent.bin");

			SentenceModel model = new SentenceModel(inputStream);

			// Instantiating the SentenceDetectorME class
			SentenceDetectorME detector = new SentenceDetectorME(model);

			// Detecting the sentence
			String sentences[] = detector.sentDetect(sentence);

			// Printing the sentences
			for (String sent : sentences)
				System.out.println(sent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void chunk() throws IOException {
		POSModel model = new POSModelLoader().load(new File("C:\\Users\\Cnd\\Downloads\\opennlpModels\\en-pos-maxent.bin"));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
		String input = "Hi. How are you? This is Mike.";
		Charset charset = Charset.forName("UTF-8");
		InputStreamFactory isf = new MarkableFileInputStreamFactory(new File("C:\\Users\\Cnd\\Desktop\\test.txt"));
		ObjectStream<String> lineStream = new PlainTextByLineStream(isf, charset);
		perfMon.start();
		String line;
		String whitespaceTokenizerLine[] = null;
		String[] tags = null;
		while ((line = lineStream.read()) != null) {
			whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
			tags = tagger.tag(whitespaceTokenizerLine);
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			System.out.println(sample.toString());
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
		// chunker
		InputStream is = new FileInputStream("C:\\Users\\Cnd\\Downloads\\opennlpModels\\en-chunker.bin");
		ChunkerModel cModel = new ChunkerModel(is);
		ChunkerME chunkerME = new ChunkerME(cModel);
		String result[] = chunkerME.chunk(whitespaceTokenizerLine, tags);
		for (String s : result)
			System.out.println(s+" hiii");
		Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine, tags);
		for (Span s : span)
			System.out.println(s.toString());
	}
	
	

}
