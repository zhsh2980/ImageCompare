/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.choices.imagecompare.holders;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.choices.imagecompare.instrumentation.Instrumented;
import com.choices.imagecompare.instrumentation.PerfListener;

/**
 * The base ViewHolder with instrumentation
 */
public abstract class BaseViewHolder<V extends View & Instrumented>
        extends RecyclerView.ViewHolder {

    protected final V mImageView;
    private final PerfListener mPerfListener;
    private final View mParentView;
    private Context mContext;

    public BaseViewHolder(
            Context context,
            View parentView,
            V imageView,
            PerfListener perfListener) {
        super(imageView);
        this.mContext = context;
        this.mPerfListener = perfListener;
        this.mParentView = parentView;
        this.mImageView = imageView;
        if (mParentView != null) {
            int size = calcDesiredSize(mParentView.getWidth(), mParentView.getHeight());
            updateViewLayoutParams(mImageView, size, size);
        }
    }

    public void bind(String model) {
        mImageView.initInstrumentation(model.toString(), mPerfListener);
        onBind(model);
    }

    /**
     * Load an image of the specified uri into the view, asynchronously.
     */
    protected abstract void onBind(String uri);

    protected Context getContext() {
        return mContext;
    }

    private void updateViewLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || layoutParams.height != width || layoutParams.width != height) {
            layoutParams = new AbsListView.LayoutParams(width, height);
            view.setLayoutParams(layoutParams);
        }
    }

    private int calcDesiredSize(int parentWidth, int parentHeight) {
        int orientation = mContext.getResources().getConfiguration().orientation;
        int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ?
                parentHeight / 2 : parentHeight / 3;
        return Math.min(desiredSize, parentWidth);
    }
}