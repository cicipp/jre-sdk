// HttpClientCallDelegate.java
// Copyright (c) 2016-2018 BuyBuddy Elektronik Güvenlik Bilişim Reklam Telekomünikasyon Sanayi ve Ticaret Limited Şirketi ( https://www.buybuddy.co/ )
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package co.buybuddy.networking.http;

import okhttp3.Response;

import javax.annotation.Generated;
import java.io.IOException;

@Generated("co.buybuddy:codegen")
public interface HttpClientCallDelegate<ReturnType> {
    public default ReturnType onContinue(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onOk(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onCreated(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onNoContent(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onBadRequest(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onNotFound(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onConflict(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }

    public default ReturnType onUnprocessableEntity(Response response) throws IOException {
        throw new UnhandledCallException(response);
    }
}