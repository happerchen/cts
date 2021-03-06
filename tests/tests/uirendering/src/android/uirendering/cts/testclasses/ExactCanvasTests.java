/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.uirendering.cts.testclasses;

import com.android.cts.uirendering.R;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.test.suitebuilder.annotation.SmallTest;
import android.uirendering.cts.bitmapcomparers.BitmapComparer;
import android.uirendering.cts.bitmapcomparers.ExactComparer;
import android.uirendering.cts.bitmapverifiers.BitmapVerifier;
import android.uirendering.cts.bitmapverifiers.RectVerifier;
import android.uirendering.cts.testinfrastructure.ActivityTestBase;
import android.uirendering.cts.testinfrastructure.CanvasClient;
import android.uirendering.cts.testinfrastructure.ViewInitializer;
import android.view.View;

public class ExactCanvasTests extends ActivityTestBase {
    private final BitmapComparer mExactComparer = new ExactComparer();

    @SmallTest
    public void testBlueRect() {
        final Rect rect = new Rect(10, 10, 100, 100);
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        p.setAntiAlias(false);
                        p.setColor(Color.BLUE);
                        canvas.drawRect(rect, p);
                    }
                })
                .runWithVerifier(new RectVerifier(Color.WHITE, Color.BLUE, rect));
    }

    @SmallTest
    public void testPoints() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        p.setAntiAlias(false);
                        p.setColor(Color.WHITE);
                        canvas.drawRect(0, 0, 100, 100, p);
                        p.setStrokeWidth(1f);
                        p.setColor(Color.BLACK);
                        for (int i = 0; i < 10; i++) {
                            canvas.drawPoint(i * 10, i * 10, p);
                        }
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testBlackRectWithStroke() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        p.setColor(Color.RED);
                        canvas.drawRect(0, 0, ActivityTestBase.TEST_WIDTH,
                                ActivityTestBase.TEST_HEIGHT, p);
                        p.setColor(Color.BLACK);
                        p.setStrokeWidth(10);
                        canvas.drawRect(10, 10, 20, 20, p);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testBlackLineOnGreenBack() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        p.setColor(Color.GREEN);
                        canvas.drawRect(0, 0, ActivityTestBase.TEST_WIDTH,
                                ActivityTestBase.TEST_HEIGHT, p);
                        p.setColor(Color.BLACK);
                        p.setStrokeWidth(10);
                        canvas.drawLine(0, 0, 50, 0, p);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testDrawRedRectOnBlueBack() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        canvas.drawColor(Color.BLUE);
                        Paint p = new Paint();
                        p.setColor(Color.RED);
                        canvas.drawRect(10, 10, 40, 40, p);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testDrawLine() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        canvas.drawColor(Color.WHITE);
                        p.setColor(Color.BLACK);
                        float[] pts = {
                                0, 0, 100, 100, 100, 0, 0, 100, 50, 50, 75, 75
                        };
                        canvas.drawLines(pts, p);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testDrawWhiteScreen() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        canvas.drawColor(Color.WHITE);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testBasicText() {
        final String testString = "THIS IS A TEST";
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        Paint p = new Paint();
                        canvas.drawColor(Color.BLACK);
                        p.setColor(Color.WHITE);
                        p.setStrokeWidth(5);
                        canvas.drawText(testString, 30, 50, p);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testBasicColorXfermode() {
        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        canvas.drawColor(Color.GRAY);
                        canvas.drawColor(Color.BLUE, PorterDuff.Mode.MULTIPLY);
                    }
                })
                .runWithComparer(mExactComparer);
    }

    @SmallTest
    public void testBluePaddedSquare() {
        final NinePatchDrawable ninePatchDrawable = (NinePatchDrawable)
            getActivity().getResources().getDrawable(R.drawable.blue_padded_square);
        ninePatchDrawable.setBounds(0, 0, 100, 100);

        BitmapVerifier verifier = new RectVerifier(Color.WHITE, Color.BLUE,
                new Rect(10, 10, 90, 90));

        createTest()
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        canvas.drawColor(Color.WHITE);
                        Paint p = new Paint();
                        p.setColor(Color.BLUE);
                        canvas.drawRect(10, 10, 90, 90, p);
                    }
                })
                .addCanvasClient(new CanvasClient() {
                    @Override
                    public void draw(Canvas canvas, int width, int height) {
                        ninePatchDrawable.draw(canvas);
                    }
                })
                .addLayout(R.layout.blue_padded_square, null)
                .runWithVerifier(verifier);
    }

    @SmallTest
    public void testClipping() {
        createTest().addLayout(R.layout.simple_red_layout, new ViewInitializer() {
            @Override
            public void intializeView(View view) {
                view.setClipBounds(new Rect(0, 0, 50, 50));
            }
        }).runWithComparer(mExactComparer);
    }
}
