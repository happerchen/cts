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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.test.suitebuilder.annotation.SmallTest;
import android.uirendering.cts.bitmapverifiers.BitmapVerifier;
import android.uirendering.cts.bitmapverifiers.ColorVerifier;
import android.uirendering.cts.bitmapverifiers.PerPixelBitmapVerifier;
import android.uirendering.cts.testinfrastructure.ActivityTestBase;
import android.uirendering.cts.testinfrastructure.CanvasClient;

public class BitmapFilterTests extends ActivityTestBase {
    private static final int THRESHOLD = 20;
    private static final int WHITE_WEIGHT = 255 * 3;
    private enum FilterEnum {
        // Creates Paint object that will have bitmap filtering
        PAINT_FILTER,
        // First uses a Paint object with bitmap filtering, then uses canvas.setDrawFilter to remove
        // the bitmap filtering
        REMOVE_FILTER,
        // Sets DrawFilter to use Paint.FILTER_BITMAP_FLAG
        ADD_FILTER
    }

    /**
     * Verifies that a Bitmap only contains white and black, within a certain threshold
     */
    private static BitmapVerifier mBlackWhiteVerifier = new PerPixelBitmapVerifier(THRESHOLD) {
        @Override
        protected boolean verifyPixel(int color, int expectedColor) {
            int weight = Color.red(color) + Color.blue(color) + Color.green(color);
            return weight < THRESHOLD // is approx Color.BLACK
                    || weight > WHITE_WEIGHT - THRESHOLD; // is approx Color.WHITE
        }
    };

    private static Bitmap createGridBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int i = 0 ; i < width ; i++) {
            for (int j = 0 ; j < height ; j++) {
                bitmap.setPixel(i, j, ((i + j * width)) % 2 == 0 ?
                        Color.WHITE : Color.BLACK);
            }
        }
        return bitmap;
    }

    private static final int SMALL_GRID_SIZE = 5;
    private static Bitmap mSmallGridBitmap = createGridBitmap(SMALL_GRID_SIZE, SMALL_GRID_SIZE);
    private static final int BIG_GRID_SIZE = 360;
    private static Bitmap mBigGridBitmap = createGridBitmap(BIG_GRID_SIZE, BIG_GRID_SIZE);
    private static final int HALFWAY_COLOR = Color.argb(255, 127, 127, 127);

    /* All of these tests seem to be broken.
     * TODO: fix in L MR1
    @SmallTest
    public void testPaintFilterScaleUp() {
        runScaleTest(FilterEnum.PAINT_FILTER, true, mBlackWhiteVerifier);
    }

    @SmallTest
    public void testPaintFilterScaleDown() {
        runScaleTest(FilterEnum.PAINT_FILTER, false, new ColorVerifier(HALFWAY_COLOR, 15));
    }

    @SmallTest
    public void testDrawFilterRemoveFilterScaleUp() {
        runScaleTest(FilterEnum.REMOVE_FILTER, true, mBlackWhiteVerifier);
    }

    @SmallTest
    public void testDrawFilterRemoveFilterScaleDown() {
        runScaleTest(FilterEnum.REMOVE_FILTER, false, mBlackWhiteVerifier);
    }

    @SmallTest
    public void testDrawFilterScaleUp() {
        runScaleTest(FilterEnum.ADD_FILTER, true, mBlackWhiteVerifier);
    }

    @SmallTest
    public void testDrawFilterScaleDown() {
        runScaleTest(FilterEnum.ADD_FILTER, false, new ColorVerifier(HALFWAY_COLOR));
    }
*/
    private void runScaleTest(final FilterEnum filterEnum, final boolean scaleUp,
            BitmapVerifier bitmapVerifier) {
        final int gridWidth = scaleUp ? SMALL_GRID_SIZE : BIG_GRID_SIZE;
        final Paint paint = new Paint(filterEnum.equals(FilterEnum.ADD_FILTER) ?
                0 : Paint.FILTER_BITMAP_FLAG);
        CanvasClient canvasClient = new CanvasClient() {
            @Override
            public void draw(Canvas canvas, int width, int height) {
                canvas.scale(1.0f * width / gridWidth, 1.0f * height / gridWidth);
                if (filterEnum.equals(FilterEnum.ADD_FILTER)) {
                    canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG));
                } else if (filterEnum.equals(FilterEnum.REMOVE_FILTER)) {
                    canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0));
                }
                canvas.drawBitmap(scaleUp ? mSmallGridBitmap : mBigGridBitmap, 0, 0, paint);
            }
        };
        createTest()
                .addCanvasClient(canvasClient)
                .runWithVerifier(bitmapVerifier);
    }
}
