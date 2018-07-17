
#ifndef Utils_h
#define Utils_h

#import <Foundation/Foundation.h>

static inline CGRect transformNormalizedBoundingRect(CGSize ScreenSize , CGRect normalizedRect){
    
    CGSize sz = CGSizeMake(normalizedRect.size.width*ScreenSize.width, normalizedRect.size.height*ScreenSize.height);
    CGPoint pt = CGPointMake(normalizedRect.origin.x*ScreenSize.width, ScreenSize.height*(1-normalizedRect.origin.y-normalizedRect.size.height));
    return (CGRect){pt,sz};
}

#endif /* Utils_h */
