import UIKit
import Vision
import AVFoundation

/*
 https://github.com/Weijay/AppleFaceDetection
 */

class PreviewView: UIImageView {
    
    private var maskLayer = [CAShapeLayer]()
    
    // Create a new layer drawing the bounding box
    private func createLayer(in rect: CGRect) -> CAShapeLayer {
        
        let mask = CAShapeLayer()
        mask.frame = rect
        
        mask.cornerRadius = 10
        mask.opacity = 0.75
        mask.borderColor = UIColor.yellow.cgColor
        mask.borderWidth = 2.0
        
        maskLayer.append(mask)
        layer.insertSublayer(mask, at: 1)
        
        return mask
    }
    
    func drawFaceWithLandmarks(face: VNFaceObservation) {
        
        let transform = CGAffineTransform(scaleX: 1, y: -1).translatedBy(x: 0, y: -frame.height)
        
        let translate = CGAffineTransform.identity.scaledBy(x: frame.width, y: frame.height)
        
        // The coordinates are normalized to the dimensions of the processed image, with the origin at the image's lower-left corner.
        let facebounds = face.boundingBox.applying(translate).applying(transform)
        
        // Draw the bounding rect
        let faceLayer = createLayer(in: facebounds)
        
        // Draw the landmarks
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.nose)!, isClosed:false)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.noseCrest)!, isClosed:false)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.medianLine)!, isClosed:false)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.leftEye)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.leftPupil)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.leftEyebrow)!, isClosed:false)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.rightEye)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.rightPupil)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.rightEye)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.rightEyebrow)!, isClosed:false)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.innerLips)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.outerLips)!)
        drawLandmarks(on: faceLayer, faceLandmarkRegion: (face.landmarks?.faceContour)!, isClosed: false)
        
    }
    
    func drawLandmarks(on targetLayer: CALayer, faceLandmarkRegion: VNFaceLandmarkRegion2D, isClosed: Bool = true) {
        let rect: CGRect = targetLayer.frame
        var points: [CGPoint] = []
        
        for i in 0..<faceLandmarkRegion.pointCount {
            let point = faceLandmarkRegion.normalizedPoints[i]
            points.append(point)
        }
        
        let landmarkLayer = drawPointsOnLayer(rect: rect, landmarkPoints: points, isClosed: isClosed)
        
        // Change scale, coordinate systems, and mirroring
        landmarkLayer.transform = CATransform3DMakeAffineTransform(
            CGAffineTransform.identity
                .scaledBy(x: rect.width, y: -rect.height)
                .translatedBy(x: 0, y: -1)
        )
        
        targetLayer.insertSublayer(landmarkLayer, at: 1)
    }
    
    func drawPointsOnLayer(rect:CGRect, landmarkPoints: [CGPoint], isClosed: Bool = true) -> CALayer {
        let linePath = UIBezierPath()
        linePath.move(to: landmarkPoints.first!)
        
        for point in landmarkPoints.dropFirst() {
            linePath.addLine(to: point)
        }
        
        if isClosed {
            linePath.addLine(to: landmarkPoints.first!)
        }
        
        let lineLayer = CAShapeLayer()
        lineLayer.path = linePath.cgPath
        lineLayer.fillColor = nil
        lineLayer.opacity = 1.0
        lineLayer.strokeColor = UIColor.green.cgColor
        lineLayer.lineWidth = 0.01
        
        return lineLayer
    }
    
    func hideMask() {
        for mask in maskLayer {
            mask.isHidden = true
        }
    }
    
    func showMask() {
        for mask in maskLayer {
            mask.isHidden = false
        }
    }
    
    // MARK: - Effects
    
    private var effectView: UIImageView!
    
    func applyEffect(_ effect: Effect, to face: VNFaceObservation) {
        
        effectView.image = effect.image
        
        let transform = CGAffineTransform(scaleX: 1, y: -1).translatedBy(x: 0, y: -frame.height)
        
        let translate = CGAffineTransform.identity.scaledBy(x: frame.width, y: frame.height)
        
        // The coordinates are normalized to the dimensions of the processed image, with the origin at the image's lower-left corner.
        let facebounds = face.boundingBox.applying(translate).applying(transform)
        let imageAspectRatio = effect.image.size.width / effect.image.size.height
        
        switch effect.type {
        case .glasses:
            
            guard let noseUpperPoint = face.landmarks?.medianLine?.normalizedPoints.first else {
                return
            }

            effectView.frame = CGRect(x: 0, y: 0, width: facebounds.width, height: imageAspectRatio * facebounds.width)
            effectView.center = CGPoint(x: facebounds.origin.x + facebounds.width * noseUpperPoint.x, y: facebounds.origin.y + facebounds.height * (1 - noseUpperPoint.y))
            
        case .lips:
            
            guard let innerLipsPoints = face.landmarks?.innerLips?.normalizedPoints,
                innerLipsPoints.count > 0 else {
                return
            }
            
            var lipsCenterPoint = innerLipsPoints.reduce(.zero, { soFar, next -> CGPoint in
                var newPoint = soFar
                newPoint.x += next.x
                newPoint.y += next.y
                return newPoint
            })
            
            lipsCenterPoint.x /= CGFloat(innerLipsPoints.count)
            lipsCenterPoint.y /= CGFloat(innerLipsPoints.count)
            
            effectView.frame = CGRect(x: 0, y: 0, width: facebounds.width / 3, height: imageAspectRatio * facebounds.width / 3)
            effectView.center = CGPoint(x: facebounds.origin.x + facebounds.width * lipsCenterPoint.x, y: facebounds.origin.y + facebounds.height * (1 - lipsCenterPoint.y))
            
        case .mustache:
            
            guard let nosePoints = face.landmarks?.nose?.normalizedPoints,
                nosePoints.count > 0 else {
                return
            }
            
            let noseBottomPoint = nosePoints.reduce(nosePoints.first!, { res, next -> CGPoint in
                next.y < res.y ? next : res
            })
            
            guard let innerLipsPoints = face.landmarks?.innerLips?.normalizedPoints,
                innerLipsPoints.count > 0 else {
                    return
            }
            
            var lipsCenterPoint = innerLipsPoints.reduce(.zero, { soFar, next -> CGPoint in
                var newPoint = soFar
                newPoint.x += next.x
                newPoint.y += next.y
                return newPoint
            })
            
            lipsCenterPoint.x /= CGFloat(innerLipsPoints.count)
            lipsCenterPoint.y /= CGFloat(innerLipsPoints.count)
            
            let mustacheCenterPoint = CGPoint(x: (noseBottomPoint.x + lipsCenterPoint.x) / 2, y: (noseBottomPoint.y + lipsCenterPoint.y) / 2)
            
            effectView.frame = CGRect(x: 0, y: 0, width: facebounds.width / 2, height: imageAspectRatio * facebounds.width / 2)
            effectView.center = CGPoint(x: facebounds.origin.x + facebounds.width * mustacheCenterPoint.x, y: facebounds.origin.y + facebounds.height * (1 - mustacheCenterPoint.y))
            
        }
        
        // this just looks bad ass with the thug life glasses
        if effect.type == .glasses {
            let actualCenter = effectView.center
            effectView.center.y = 0
            UIView.animate(withDuration: 1, delay: 0.2, usingSpringWithDamping: 10, initialSpringVelocity: 25, options: .curveEaseIn, animations: {
                self.effectView.center = actualCenter
            }, completion: nil)
        }
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let rotateGestureRecognizer = UIRotationGestureRecognizer(target: self, action: #selector(rotate(_:)))
        rotateGestureRecognizer.delegate = self
        addGestureRecognizer(rotateGestureRecognizer)
        let panGestureRecognizer = UIPanGestureRecognizer(target: self, action: #selector(pan(_:)))
        panGestureRecognizer.delegate = self
        addGestureRecognizer(panGestureRecognizer)
        let pinchGestureRecognizer = UIPinchGestureRecognizer(target: self, action: #selector(pinch(_:)))
        pinchGestureRecognizer.delegate = self
        addGestureRecognizer(pinchGestureRecognizer)
        
        effectView = UIImageView()
        effectView.contentMode = .scaleAspectFit
        addSubview(effectView)
        
    }
    
    @objc func rotate(_ gestureRecognizer: UIRotationGestureRecognizer) {
        effectView.transform = effectView.transform.rotated(by: gestureRecognizer.rotation)
        gestureRecognizer.rotation = 0
    }
    
    @objc func pan(_ gestureRecognzier: UIPanGestureRecognizer) {
        let translation = gestureRecognzier.translation(in: self)
        effectView.center.x += translation.x
        effectView.center.y += translation.y
        gestureRecognzier.setTranslation(.zero, in: self)
    }
    
    @objc func pinch(_ gestureRecognzier: UIPinchGestureRecognizer) {
        effectView.transform = effectView.transform.scaledBy(x: gestureRecognzier.scale, y: gestureRecognzier.scale)
        gestureRecognzier.scale = 1
    }
    
}

extension PreviewView: UIGestureRecognizerDelegate {
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool {
        return true
    }
    
}
