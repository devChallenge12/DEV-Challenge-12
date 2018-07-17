
#import "DCGridCell.h"
#import "DCCategoryEffectModel.h"


@interface DCGridCell ()
@property (nonatomic, strong) UIImageView *imageItemView;

@end

@implementation DCGridCell

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setup];
    }
    return self;
}

- (void)setup {
    self.imageItemView = ({
        UIImageView *v = [UIImageView new];
        v.contentMode = UIViewContentModeScaleAspectFit;
        [self.contentView addSubview:v];
        v;
    });
   
}

- (void)setItem:(DCCategoryEffectModel *)item {
    _item = item;
    DCCategoryEffectModel *model = _item;
    self.imageItemView.image = [UIImage imageNamed:model.imageName];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.imageItemView.frame = CGRectMake(0, 0, CGRectGetWidth(self.bounds),
                                          CGRectGetHeight(self.bounds));
}

@end
