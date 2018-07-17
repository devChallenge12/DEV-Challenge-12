//
//  DrawingExtension.swift
//  DevchallengeFinal
//
//  Created by Someone on 6/23/18.
//  Copyright © 2018 Someone. All rights reserved.
//

import UIKit
import Vision

extension ViewController {
    func drawMustacheOnImage(source: UIImage, boundingRect: CGRect, lips: VNFaceLandmarkRegion2D, nose: VNFaceLandmarkRegion2D, withImage image: UIImage) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(source.size, false, 1)
        let context = UIGraphicsGetCurrentContext()!
        context.translateBy(x: 0, y: source.size.height)
        context.scaleBy(x: 1.0, y: -1.0)
        context.setBlendMode(CGBlendMode.colorBurn)
        context.setLineJoin(.round)
        context.setLineCap(.round)
        context.setShouldAntialias(true)
        context.setAllowsAntialiasing(true)
        
        let rectWidth = source.size.width * boundingRect.size.width
        let rectHeight = source.size.height * boundingRect.size.height
        
        //draw image
        let rect = CGRect(x: 0, y: 0, width: source.size.width, height: source.size.height)
        context.draw(source.cgImage!, in: rect)
        
        //draw overlay
        let fillColor = UIColor.red
        fillColor.setFill()
        context.setLineWidth(2.0)
        var lipsPoints: [CGPoint] = []
        for i in 0..<lips.pointCount {
            let point = lips.normalizedPoints[i]
            let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
            lipsPoints.append(p)
        }
        var mappedPoints = lipsPoints.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
        
        let minX = mappedPoints.min(by: {$0.x < $1.x})!.x
        let minY = mappedPoints.min(by: {$0.y < $1.y})!.y
        let maxX = mappedPoints.max(by: {$0.x < $1.x})!.x
        let maxY = mappedPoints.max(by: {$0.y < $1.y})!.y
        
        var nosePoints: [CGPoint] = []
        for i in 0..<nose.pointCount {
            let point = nose.normalizedPoints[i]
            let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
            nosePoints.append(p)
        }
        mappedPoints = nosePoints.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
        
        let minYPointOfNose = mappedPoints.min(by: {$0.y < $1.y})!.y
        
        context.draw(image.cgImage!, in: CGRect(x: minX, y: minY - (maxY - minYPointOfNose), width: maxX-minX, height: maxY-minY))
        
        let coloredImg: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return coloredImg
    }
    
    func drawFaceRectangle(source: UIImage, boundingRect: CGRect) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(source.size, false, 1)
        let context = UIGraphicsGetCurrentContext()!
        context.translateBy(x: 0, y: source.size.height)
        context.scaleBy(x: 1.0, y: -1.0)
        context.setBlendMode(CGBlendMode.colorBurn)
        context.setLineJoin(.round)
        context.setLineCap(.round)
        context.setShouldAntialias(true)
        context.setAllowsAntialiasing(true)
        
        let rectWidth = source.size.width * boundingRect.size.width
        let rectHeight = source.size.height * boundingRect.size.height
        
        //draw image
        let rect = CGRect(x: 0, y: 0, width: source.size.width, height: source.size.height)
        context.draw(source.cgImage!, in: rect)
        
        //draw bound rect
        let fillColor = UIColor.red
        fillColor.setStroke()
        context.setLineWidth(8.0)
        context.addRect(CGRect(x: boundingRect.origin.x * source.size.width, y:boundingRect.origin.y * source.size.height, width: rectWidth, height: rectHeight))
        context.drawPath(using: CGPathDrawingMode.stroke)
        
        let coloredImg : UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return coloredImg
    }
    
    func drawGlassesOnImage(source: UIImage,
                            boundingRect: CGRect,
                            leftEye: VNFaceLandmarkRegion2D, rightEye: VNFaceLandmarkRegion2D, leftEyeBrow: VNFaceLandmarkRegion2D, withImage image: UIImage) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(source.size, false, 1)
        let context = UIGraphicsGetCurrentContext()!
        context.translateBy(x: 0, y: source.size.height)
        context.scaleBy(x: 1.0, y: -1.0)
        context.setBlendMode(CGBlendMode.colorBurn)
        context.setLineJoin(.round)
        context.setLineCap(.round)
        context.setShouldAntialias(true)
        context.setAllowsAntialiasing(true)
        
        let rectWidth = source.size.width * boundingRect.size.width
        let rectHeight = source.size.height * boundingRect.size.height
        
        //draw image
        let rect = CGRect(x: 0, y: 0, width: source.size.width, height: source.size.height)
        context.draw(source.cgImage!, in: rect)
        
        //draw overlay
        let fillColor = UIColor.red
        fillColor.setFill()
        context.setLineWidth(2.0)
        var allPoints = [CGPoint]()
        var pointsLeftEye: [CGPoint] = []
        for i in 0..<leftEye.pointCount {
            let point = leftEye.normalizedPoints[i]
            let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
            pointsLeftEye.append(p)
        }
        var mappedPoints = pointsLeftEye.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
        allPoints.append(contentsOf: mappedPoints)
        
        let minX = allPoints.min(by: {$0.x < $1.x})!.x
        
        var pointsRightEye: [CGPoint] = []
        for i in 0..<rightEye.pointCount {
            let point = rightEye.normalizedPoints[i]
            let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
            pointsRightEye.append(p)
        }
        mappedPoints = pointsRightEye.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
        allPoints.append(contentsOf: mappedPoints)
        
        var pointsLeftEyeBrow: [CGPoint] = []
        for i in 0..<leftEyeBrow.pointCount {
            let point = leftEyeBrow.normalizedPoints[i]
            let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
            pointsLeftEyeBrow.append(p)
        }
        mappedPoints = pointsLeftEyeBrow.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
        
        let maxX = allPoints.max(by: {$0.x < $1.x})!.x
        let minY = allPoints.min(by: {$0.y < $1.y})!.y
        
        let maxYLeftEyeBrow = mappedPoints.max(by: {$0.y < $1.y})!.y
        
        context.draw(image.cgImage!, in: CGRect(x: minX-20, y: minY-20, width: maxX-minX+40, height: maxYLeftEyeBrow-minY))
        
        let coloredImg: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return coloredImg
    }
    
    func drawLipsOnImage(source: UIImage,
                         boundingRect: CGRect,
                         faceLandmarkRegions: [VNFaceLandmarkRegion2D], withImage image: UIImage) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(source.size, false, 1)
        let context = UIGraphicsGetCurrentContext()!
        context.translateBy(x: 0, y: source.size.height)
        context.scaleBy(x: 1.0, y: -1.0)
        context.setBlendMode(CGBlendMode.colorBurn)
        context.setLineJoin(.round)
        context.setLineCap(.round)
        context.setShouldAntialias(true)
        context.setAllowsAntialiasing(true)
        
        let rectWidth = source.size.width * boundingRect.size.width
        let rectHeight = source.size.height * boundingRect.size.height
        
        //draw image
        let rect = CGRect(x: 0, y: 0, width: source.size.width, height: source.size.height)
        context.draw(source.cgImage!, in: rect)
        
        //draw overlay
        let fillColor = UIColor.red
        fillColor.setFill()
        context.setLineWidth(2.0)
        for faceLandmarkRegion in faceLandmarkRegions {
            var points: [CGPoint] = []
            for i in 0..<faceLandmarkRegion.pointCount {
                let point = faceLandmarkRegion.normalizedPoints[i]
                let p = CGPoint(x: CGFloat(point.x), y: CGFloat(point.y))
                points.append(p)
            }
            let mappedPoints = points.map { CGPoint(x: boundingRect.origin.x * source.size.width + $0.x * rectWidth, y: boundingRect.origin.y * source.size.height + $0.y * rectHeight) }
            let minX = mappedPoints.min(by: {$0.x < $1.x})!.x
            let minY = mappedPoints.min(by: {$0.y < $1.y})!.y
            let maxX = mappedPoints.max(by: {$0.x < $1.x})!.x
            let maxY = mappedPoints.max(by: {$0.y < $1.y})!.y
            context.draw(image.cgImage!, in: CGRect(x: minX, y: minY, width: maxX-minX, height: maxY-minY))
            context.drawPath(using: CGPathDrawingMode.fill)
        }
        
        let coloredImg: UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return coloredImg
    }
}
