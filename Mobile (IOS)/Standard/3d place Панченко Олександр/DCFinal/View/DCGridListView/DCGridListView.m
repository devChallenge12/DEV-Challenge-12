
#import "DCGridListView.h"
#import "DCGridCell.h"

@interface DCGridListView () <UICollectionViewDelegate, UICollectionViewDataSource>

@end

@implementation DCGridListView

- (id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout {
	self = [super initWithFrame:frame collectionViewLayout:layout];
    if (self) {
        [self setup];
    }
    return self;
}

- (void)setup {
    self.backgroundColor = [UIColor clearColor];
    self.delegate = self;
    self.dataSource = self;
    self.showsHorizontalScrollIndicator = NO;
    self.numberOfRows = 1;
    [self registerClass:[DCGridCell class] forCellWithReuseIdentifier:NSStringFromClass([DCGridCell class])];
	
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView
                        layout:(UICollectionViewLayout *)collectionViewLayout
        insetForSectionAtIndex:(NSInteger)section {
	
	return UIEdgeInsetsMake(0, 0, 0, 0);
}


- (void)setItems:(NSArray *)items {
    _items = items;
    [self reloadData];
    
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.items.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    DCGridCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:NSStringFromClass([DCGridCell class]) forIndexPath:indexPath];
    cell.item = [self.items objectAtIndex:indexPath.row];
    return cell;
}

- (void)setFrame:(CGRect)frame {
    [super setFrame:frame];
    
    UICollectionViewFlowLayout *f = [UICollectionViewFlowLayout new];
    f.minimumInteritemSpacing = 0;
    f.minimumLineSpacing = 0;
	f.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    f.itemSize = CGSizeMake( CGRectGetWidth(self.frame)/2.0, CGRectGetHeight(self.frame) / self.numberOfRows);
    self.collectionViewLayout = f;
    
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (self.selectedItemCallback) {
        self.selectedItemCallback([self.items objectAtIndex:indexPath.row]);
        
    }
}

- (CGSize)contentSize {
    return CGSizeMake(ceil(self.items.count / (2 * self.numberOfRows)) * CGRectGetWidth(self.frame), CGRectGetHeight(self.frame));
}

@end

