
#import <Foundation/Foundation.h>

typedef NS_OPTIONS(NSUInteger, DCBaseModelType) {
    DCBaseModelTypeGlasses = 0,
    DCBaseModelTypeLips = 1,
    DCBaseModelTypeMustache = 2,
};


@interface DCBaseModel : NSObject
@property (nonatomic, strong) NSString *imageName;
@property (nonatomic, assign) DCBaseModelType type;

@end
