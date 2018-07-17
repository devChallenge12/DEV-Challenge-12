
#import "DCViewController.h"
#import <AVFoundation/AVFoundation.h>
#import "VisionDetector.h"
#import "Utils.h"
#import "JSImagePickerViewController.h"
#import "DCGridListView.h"
#import "DCCategoryEffectModel.h"

static const NSInteger kFaceRectangle = 10001;
static const NSInteger kEffectTag = 10002;

@interface DCViewController () <JSImagePickerViewControllerDelegate>

@property (nonatomic, strong) UIImageView *faceView;
@property (nonatomic, strong) UIButton *selectImageButton;
@property (nonatomic, strong) UIButton *saveFotoToLibraryButton;
@property (nonatomic, strong) DCGridListView *categoryCollectionView;
@property (nonatomic, strong) DCGridListView *effectCollectionView;

@property (nonatomic, strong) NSArray *categoryArray;

@property (nonatomic, strong) NSArray *lipsArray;
@property (nonatomic, strong) NSArray *mustacheArray;
@property (nonatomic, strong) NSArray *glassesArray;

@property (nonatomic, strong) UIImageView *effectImageView;
@property (nonatomic, assign) BOOL effectSelected;
@property (nonatomic, assign) BOOL isDetectedFace;
@property (nonatomic, assign) BOOL dragging;

@property (nonatomic, assign) CGFloat oldX;
@property (nonatomic, assign) CGFloat oldY;
@end

@implementation DCViewController{
    VisionDetector *_faceDetector;
}

#pragma mark - Lazzy Load

- (NSArray *)categoryArray {
    if (!_categoryArray) {
        DCCategoryEffectModel *m1 = [DCCategoryEffectModel new];
        m1.imageName = @"glasses1";
        DCCategoryEffectModel *m2 = [DCCategoryEffectModel new];
        m2.imageName = @"lips1";
        DCCategoryEffectModel *m3 = [DCCategoryEffectModel new];
        m3.imageName = @"mustache1";
        _categoryArray = [@[m1, m2, m3] mutableCopy];
    }
    return _categoryArray;
}

- (NSArray *)lipsArray {
    if (!_lipsArray) {
        NSMutableArray *a = [NSMutableArray array];
        for (int i = 1; i<4; i++) {
            DCBaseModel *m = [DCBaseModel new];
            m.imageName = [NSString stringWithFormat:@"lips%@", @(i).stringValue];
            m.type = DCBaseModelTypeLips;
            [a addObject:m];
            _lipsArray = [a mutableCopy];
        }
    }
    
    return _lipsArray;
}

- (NSArray *)glassesArray {
    if (!_glassesArray) {
        NSMutableArray *a = [NSMutableArray array];
        for (int i = 1; i<12; i++) {
            DCBaseModel *m = [DCBaseModel new];
            m.imageName = [NSString stringWithFormat:@"glasses%@", @(i).stringValue];
            m.type = DCBaseModelTypeGlasses;
            [a addObject:m];
            _glassesArray = [a mutableCopy];
        }
    }
    
    return _glassesArray;
}

- (NSArray *)mustacheArray {
    if (!_mustacheArray) {
        NSMutableArray *a = [NSMutableArray array];
        for (int i = 1; i<6; i++) {
            DCBaseModel *m = [DCBaseModel new];
            m.imageName = [NSString stringWithFormat:@"mustache%@", @(i).stringValue];
            m.type = DCBaseModelTypeGlasses;
            [a addObject:m];
            _mustacheArray = [a mutableCopy];
        }
    }
    
    return _mustacheArray;
}

#pragma mark - Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.selectImageButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.selectImageButton addTarget:self
                               action:@selector(showBtnWasPressed:)
                     forControlEvents:UIControlEventTouchUpInside];
    [self.selectImageButton setTitle:@"SHOW IMAGE PICKER" forState:UIControlStateNormal];
    
    self.saveFotoToLibraryButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.saveFotoToLibraryButton addTarget:self
                                     action:@selector(saveFoto)
                           forControlEvents:UIControlEventTouchUpInside];
    [self.saveFotoToLibraryButton setTitle:@"SAVE FOTO" forState:UIControlStateNormal];
    self.saveFotoToLibraryButton.layer.borderWidth = 1.0;
    self.saveFotoToLibraryButton.hidden = YES;
    [self.view addSubview:self.saveFotoToLibraryButton];
    
    self.selectImageButton.layer.borderWidth = 1.0;
    self.faceView.layer.borderColor = [UIColor whiteColor].CGColor;
    [self.view addSubview:self.selectImageButton];
    
    self.faceView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"image_face.jpg"]];
    self.faceView.layer.borderWidth = 1.0;
    self.faceView.layer.borderColor = [UIColor whiteColor].CGColor;
    self.faceView.userInteractionEnabled = YES;
    self.faceView.clipsToBounds = YES;
    
    [self.view addSubview:self.faceView];
    
    self.effectImageView = [UIImageView new];
    self.effectImageView.frame = CGRectMake(0, 0, 100, 70);
    self.effectImageView.hidden = YES;
    self.effectImageView.tag = kEffectTag;
    self.effectImageView.userInteractionEnabled = YES;
    
    UIPanGestureRecognizer *pan = [[UIPanGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(handlePan:)];
    [pan setMinimumNumberOfTouches:1];
    [pan setMaximumNumberOfTouches:1];
    [self.effectImageView addGestureRecognizer:pan];
    
    UIPinchGestureRecognizer *pinch = [[UIPinchGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(fingerPinch:)];
    [self.effectImageView addGestureRecognizer:pinch];
    self.effectImageView.userInteractionEnabled = YES;
    
    self.effectImageView.userInteractionEnabled = YES;
    [self.faceView addSubview:self.effectImageView];

    __weak typeof(self) weakSelf = self;
    self.categoryCollectionView = ({
        DCGridListView *v = [[DCGridListView alloc] initWithFrame:CGRectZero collectionViewLayout:[UICollectionViewLayout new]];
        v.selectedItemCallback = ^(DCCategoryEffectModel *item) {
            __strong typeof(self) strongSelf = weakSelf;
            if (!strongSelf) {
                return;
            }
            strongSelf.effectCollectionView.hidden = NO;
            
            if ([item.imageName isEqualToString:@"glasses1"]) {
                strongSelf.effectCollectionView.items = self.glassesArray;
            } else if ([item.imageName isEqualToString:@"lips1"]) {
                strongSelf.effectCollectionView.items = self.lipsArray;
            } else if ([item.imageName isEqualToString:@"mustache1"]) {
                strongSelf.effectCollectionView.items = self.mustacheArray;
            }
        };
        v.pagingEnabled = YES;
        v.backgroundColor = [UIColor whiteColor];
        [self.view addSubview:v];
        v;
    });
   
    self.effectCollectionView = ({
        DCGridListView *v = [[DCGridListView alloc] initWithFrame:CGRectZero collectionViewLayout:[UICollectionViewLayout new]];
        v.selectedItemCallback = ^(DCBaseModel *item) {
            __strong typeof(self) strongSelf = weakSelf;
            if (!strongSelf) {
                return;
            }
            strongSelf.effectCollectionView.hidden = YES;
            strongSelf.effectImageView.image = [UIImage imageNamed:item.imageName];
            strongSelf.effectImageView.center = self.faceView.center;
            strongSelf.effectImageView.hidden = NO;
            strongSelf.saveFotoToLibraryButton.hidden = NO;
        };
        v.pagingEnabled = YES;
        v.hidden = YES;
        v.backgroundColor = [UIColor whiteColor];
        [self.view addSubview:v];
        v;
    });
    
    self.categoryCollectionView.frame = CGRectMake(20, CGRectGetHeight(self.view.frame) - 100, CGRectGetWidth(self.view.frame) - 40, 60);
    
    self.effectCollectionView.frame = CGRectMake(20, CGRectGetMinY(self.categoryCollectionView.frame) - 61, CGRectGetWidth(self.view.frame) - 40, 60);
    
    self.effectCollectionView.items = self.lipsArray;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
         __strong typeof(self) strongSelf = weakSelf;
        strongSelf->_faceDetector = [[VisionDetector alloc]initWithARSession:nil];
        __weak DCViewController* weakSelf = self;
        [strongSelf->_faceDetector detectingFaceImage:self.faceView.image completion:^(CGRect normalizedRect, NSString* name) {
            if ([name isEqualToString:@"unknown"]) {
                NSLog(@"unknown face");
            } else {
                if (!self.effectSelected && !self.isDetectedFace) {
                    [weakSelf updateFrame:normalizedRect];
                    self.isDetectedFace = YES;
                }
            }
        }];
    });
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    self.categoryCollectionView.items = self.categoryArray;
}

- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    self.faceView.frame = CGRectMake(20, 20, CGRectGetWidth(self.view.bounds) - 40, 300);
    self.selectImageButton.frame = CGRectMake(20,  CGRectGetMaxY(self.faceView.frame) + 20, CGRectGetWidth(self.view.bounds) - 40, 50);
    self.saveFotoToLibraryButton.frame = CGRectMake(20,  CGRectGetMaxY(self.selectImageButton.frame) + 20, CGRectGetWidth(self.view.bounds) - 40, 50);
}

#pragma - mark GestureRecognizer

- (void)handlePan:(UIPanGestureRecognizer *)panGestureRecognizer {
    if (panGestureRecognizer.state == UIGestureRecognizerStateChanged) {
        CGPoint center = panGestureRecognizer.view.center;
        CGPoint translation = [panGestureRecognizer translationInView:panGestureRecognizer.view];
        center = CGPointMake(center.x + translation.x,
                             center.y + translation.y);
        panGestureRecognizer.view.center = center;
        [panGestureRecognizer setTranslation:CGPointZero inView:panGestureRecognizer.view];
    }
}

- (void)fingerPinch:(UIPinchGestureRecognizer *)recognizer {
    CGAffineTransform transform = CGAffineTransformMakeScale([recognizer scale],  [recognizer scale]);
    recognizer.view.transform = CGAffineTransformScale(recognizer.view.transform, recognizer.scale, 1);
    recognizer.scale = 1;
}

- (void)updateFrame:(CGRect)rect {
    [[self.faceView viewWithTag:kFaceRectangle] removeFromSuperview];
    CGRect faceRect = transformNormalizedBoundingRect(self.faceView.bounds.size, rect);
    UIView *view = [[UIView alloc] initWithFrame:faceRect];
    view.tag = kFaceRectangle;
    view.userInteractionEnabled = YES;
    view.layer.borderWidth = 3.0;
    view.layer.borderColor = [UIColor whiteColor].CGColor;
    [self.faceView insertSubview:view belowSubview:self.effectImageView];
}

#pragma - mark Button events

- (void)showBtnWasPressed:(id)sender {
    [[self.faceView viewWithTag:kFaceRectangle] removeFromSuperview];
    JSImagePickerViewController *imagePicker = [[JSImagePickerViewController alloc] init];
    imagePicker.delegate = self;
    [imagePicker showImagePickerInController:self animated:YES];
}

- (void)saveFoto {
    [[self.faceView viewWithTag:kFaceRectangle] removeFromSuperview];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:
                                @"Поздравляем!" message:@"Фото успешно сохранено в библиотеку" preferredStyle:UIAlertActionStyleCancel];
    UIAlertAction *ok = [UIAlertAction actionWithTitle:@"Ok" style:UIAlertActionStyleCancel
                                               handler:^(UIAlertAction * _Nonnull action) {
                                                   self.saveFotoToLibraryButton.hidden = YES;
                                               }];
    [alert addAction:ok];
    [self presentViewController:alert animated:YES completion:nil];
    
    UIView *subView = self.faceView;
    UIGraphicsBeginImageContextWithOptions(subView.bounds.size, YES, 0.0f);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [subView.layer renderInContext:context];
    UIImage *snapshotImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    UIImageWriteToSavedPhotosAlbum(snapshotImage, nil, nil,nil);
}

#pragma mark - JSImagePikcerViewControllerDelegate

- (void)imagePicker:(JSImagePickerViewController *)imagePicker didSelectImage:(UIImage *)image {
    [[self.faceView viewWithTag:kFaceRectangle] removeFromSuperview];
    self.faceView.image = image;
    self.isDetectedFace = NO;
    self.effectSelected = NO;
}

@end
