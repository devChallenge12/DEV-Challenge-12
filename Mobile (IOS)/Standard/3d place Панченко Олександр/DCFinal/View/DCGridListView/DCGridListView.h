
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void (^SESliderDidChangePage)(NSInteger page);
typedef void (^SEGridViewItemSelectedCallback)(id item);

@interface DCGridListView : UICollectionView
@property (strong) SEGridViewItemSelectedCallback selectedItemCallback;


@property (nonatomic, strong) NSArray *items;
@property (strong) Class cellClass;

@property (assign) CGSize requiredSize;
@property (assign) NSInteger numberOfRows;
@property (strong) UICollectionView *collectionView;

@end
