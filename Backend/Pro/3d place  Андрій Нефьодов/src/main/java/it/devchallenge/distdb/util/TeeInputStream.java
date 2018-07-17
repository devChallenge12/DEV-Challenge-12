package it.devchallenge.distdb.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends InputStream {
    protected InputStream source;
    protected OutputStream copySink;

    public TeeInputStream(InputStream source, OutputStream sink) {
        this.copySink = sink;
        this.source = source;
    }

    public int read() throws IOException {
        int result = this.source.read();
        this.copySink.write(result);
        return result;
    }

    public int available() throws IOException {
        return this.source.available();
    }

    public void close() throws IOException {
        this.source.close();
    }

    public synchronized void mark(int readlimit) {
        this.source.mark(readlimit);
    }

    public boolean markSupported() {
        return this.source.markSupported();
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int result = this.source.read(b, off, len);
        this.copySink.write(b, off, len);
        return result;
    }

    public int read(byte[] b) throws IOException {
        int result = this.source.read(b);
        this.copySink.write(b);
        return result;
    }

    public synchronized void reset() throws IOException {
        this.source.reset();
    }

    public long skip(long n) throws IOException {
        return this.source.skip(n);
    }
}
