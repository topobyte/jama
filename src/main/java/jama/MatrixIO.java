package jama;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class MatrixIO
{

	/**
	 * Print the matrix to stdout. Line the elements up in columns with a
	 * Fortran-like 'Fw.d' style format.
	 * 
	 * @param w
	 *            Column width.
	 * @param d
	 *            Number of digits after the decimal.
	 * @param m
	 *            The matrix to print.
	 */

	public static void print(int w, int d, Matrix m)
	{
		print(new PrintWriter(System.out, true), w, d, m);
	}

	/**
	 * Print the matrix to the output stream. Line the elements up in columns
	 * with a Fortran-like 'Fw.d' style format.
	 * 
	 * @param output
	 *            Output stream.
	 * @param w
	 *            Column width.
	 * @param d
	 *            Number of digits after the decimal.
	 * @param m
	 *            The matrix to print.
	 */

	public static void print(PrintWriter output, int w, int d, Matrix m)
	{
		DecimalFormat format = new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		format.setMinimumIntegerDigits(1);
		format.setMaximumFractionDigits(d);
		format.setMinimumFractionDigits(d);
		format.setGroupingUsed(false);
		print(output, format, w + 2, m);
	}

	/**
	 * Print the matrix to stdout. Line the elements up in columns. Use the
	 * format object, and right justify within columns of width characters. Note
	 * that is the matrix is to be read back in, you probably will want to use a
	 * NumberFormat that is set to US Locale.
	 * 
	 * @param format
	 *            A Formatting object for individual elements.
	 * @param width
	 *            Field width for each column.
	 * @param m
	 *            The matrix to print.
	 * @see java.text.DecimalFormat#setDecimalFormatSymbols
	 */

	public static void print(NumberFormat format, int width, Matrix m)
	{
		print(new PrintWriter(System.out, true), format, width, m);
	}

	// DecimalFormat is a little disappointing coming from Fortran or C's
	// printf.
	// Since it doesn't pad on the left, the elements will come out different
	// widths. Consequently, we'll pass the desired column width in as an
	// argument and do the extra padding ourselves.

	/**
	 * Print the matrix to the output stream. Line the elements up in columns.
	 * Use the format object, and right justify within columns of width
	 * characters. Note that is the matrix is to be read back in, you probably
	 * will want to use a NumberFormat that is set to US Locale.
	 * 
	 * @param output
	 *            the output stream.
	 * @param format
	 *            A formatting object to format the matrix elements
	 * @param width
	 *            Column width.
	 * @param m
	 *            The matrix to print.
	 * @see java.text.DecimalFormat#setDecimalFormatSymbols
	 */

	public static void print(PrintWriter output, NumberFormat format, int width,
			Matrix m)
	{
		output.println(); // start on new line.
		for (int i = 0; i < m.getRowDimension(); i++) {
			for (int j = 0; j < m.getColumnDimension(); j++) {
				String s = format.format(m.get(i, j)); // format the number
				int padding = Math.max(1, width - s.length()); // At _least_ 1
																// space
				for (int k = 0; k < padding; k++) {
					output.print(' ');
				}
				output.print(s);
			}
			output.println();
		}
		output.println(); // end with blank line.
	}

	/**
	 * Read a matrix from a stream. The format is the same the print method, so
	 * printed matrices can be read back in (provided they were printed using US
	 * Locale). Elements are separated by whitespace, all the elements for each
	 * row appear on a single line, the last row is followed by a blank line.
	 * 
	 * @param input
	 *            the input stream.
	 */

	public static Matrix read(BufferedReader input) throws java.io.IOException
	{
		StreamTokenizer tokenizer = new StreamTokenizer(input);

		// Although StreamTokenizer will parse numbers, it doesn't recognize
		// scientific notation (E or D); however, Double.valueOf does.
		// The strategy here is to disable StreamTokenizer's number parsing.
		// We'll only get whitespace delimited words, EOL's and EOF's.
		// These words should all be numbers, for Double.valueOf to parse.

		tokenizer.resetSyntax();
		tokenizer.wordChars(0, 255);
		tokenizer.whitespaceChars(0, ' ');
		tokenizer.eolIsSignificant(true);
		java.util.Vector<Double> vD = new java.util.Vector<Double>();

		// Ignore initial empty lines
		while (tokenizer.nextToken() == StreamTokenizer.TT_EOL) {
			;
		}
		if (tokenizer.ttype == StreamTokenizer.TT_EOF) {
			throw new java.io.IOException("Unexpected EOF on matrix read.");
		}
		do {
			vD.addElement(Double.valueOf(tokenizer.sval)); // Read & store 1st
															// row.
		} while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

		int n = vD.size(); // Now we've got the number of columns!
		double row[] = new double[n];
		// extract the elements of the 1st row.
		for (int j = 0; j < n; j++) {
			row[j] = vD.elementAt(j).doubleValue();
		}
		java.util.Vector<double[]> v = new java.util.Vector<double[]>();
		v.addElement(row); // Start storing rows instead of columns.
		while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
			// While non-empty lines
			v.addElement(row = new double[n]);
			int j = 0;
			do {
				if (j >= n) {
					throw new java.io.IOException(
							"Row " + v.size() + " is too long.");
				}
				row[j++] = Double.valueOf(tokenizer.sval).doubleValue();
			} while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
			if (j < n) {
				throw new java.io.IOException(
						"Row " + v.size() + " is too short.");
			}
		}
		int m = v.size(); // Now we've got the number of rows.
		double[][] A = new double[m][];
		v.copyInto(A); // copy the rows out of the vector
		return new Matrix(A);
	}

}
