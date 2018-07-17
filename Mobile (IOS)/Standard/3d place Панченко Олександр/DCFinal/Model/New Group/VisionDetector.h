
// source: https://github.com/xta0/ARML/blob/eec43a183d9300d6625c5e234c960c2a8522df79/MLAR/MLAR/VisionDetector.h

#import <UIKit/UIKit.h>

@class ARSession;
@interface VisionDetector : NSObject

- (id)initWithARSession:(ARSession* )session;
- (void)detectingFaceImage:(UIImage *)image completion:(void (^)(CGRect, NSString *))result;


@end
